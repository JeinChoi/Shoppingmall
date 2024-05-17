package com.shoppingmall.productservice.service;

import com.shoppingmall.productservice.config.RedissonLock;
import com.shoppingmall.productservice.domain.Item;
import com.shoppingmall.productservice.dto.ItemUpdateDto;
import com.shoppingmall.productservice.dto.ManageProductDto;
import com.shoppingmall.productservice.dto.UpdateStockDto;
import com.shoppingmall.productservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final RedisService redisService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ItemService.class);

    public Optional<Item> findOne(Long itemId) {
       return itemRepository.findById(itemId);
    }


    public void updateState(Long itemId){
        Item findItem = itemRepository.findById(itemId).get();
        findItem.updateStateToSoldout();

    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    //만약에 주문 취소 된 경우에는 - . 혹은 주문 완료된 경우에는 +
    public void updateStock(UpdateStockDto updateStockDto){
    //여기서 분산락 처리 하는 메서드 호출 해주기 근데 db는....
        updateInRedis(updateStockDto);
    }
    @RedissonLock(value="#itemId")
    private void updateInRedis(UpdateStockDto updateStockDto){

        int presentStock = Integer.parseInt(redisService.getValues(updateStockDto.getItemId()+""));

        logger.info("현재 재고 량 :: {}",presentStock);
        try{
            if(presentStock<updateStockDto.getCount())
                throw new RuntimeException("주문 수량이 재고 수량보다 많습니다");
            if(presentStock==0){
                Item item = findOne(updateStockDto.getItemId()).get();
                item.updateStateToSoldout();
                throw new RuntimeException("품절 상태입니다.");
            }
        }catch(Exception e){
            System.err.println("오류: "+e.getMessage());
        }

        long updateStock = presentStock+(updateStockDto.getCount()*(updateStockDto.isPlus()?1:-1));
        if(updateStock==0) {
            updateState(updateStockDto.getItemId());
        }
        redisService.setValues(updateStockDto.getItemId()+"",updateStock+""); //만약 여기서 false가 반환되면 item 상태는 품절로 바뀌고 주문 실패 띄우기

        //mysql update
//        Item findItem = itemRepository.findById(updateStockDto.getItemId()).get();
//        findItem.updateStock(updateStockDto.getCount(),updateStockDto.isPlus());

    }


}
