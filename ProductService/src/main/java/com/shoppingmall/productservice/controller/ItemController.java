package com.shoppingmall.productservice.controller;

import com.shoppingmall.productservice.domain.Item;
import com.shoppingmall.productservice.dto.*;
import com.shoppingmall.productservice.dto.feignClientDto.ItemFeignResponse;
import com.shoppingmall.productservice.repository.ItemListRepository;
import com.shoppingmall.productservice.service.ItemService;
import com.shoppingmall.productservice.service.RedisService;
import com.shoppingmall.productservice.shopinfo.ShopInfoSearch;
import jakarta.inject.Qualifier;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ShopInfoSearch shopInfoSearch;
    private final ItemListRepository itemListRepository;
    private final ItemService itemService;
    private final RedisService redisService;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    @GetMapping("/listdata")//db 저장용
    public ResponseEntity<?> getShoppingInfo() {

        String result = shopInfoSearch.search();

        List<Item> list = shopInfoSearch.fromJSONtoItems(result);
        itemListRepository.saveAll(list);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @PostMapping("/storage")
    public ResponseEntity<?> manageStorage(){
        List<Item> list = itemService.findAll();
        for(Item item : list){
            redisService.setValues((item.getItemId()+""),(item.getStockQuantity()+""));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/stock/{itemId}")
    public int stockQuantity(@PathVariable(name="itemId") String itemId){
        return Integer.parseInt(redisService.getValues(itemId));
    }

    @PostMapping("/updateState")
    public void updateState(@RequestBody ItemUpdateDto itemUpdateDto){
        itemService.updateState(itemUpdateDto.getItemId());
    }
    //상품 리스트와 상세 조회 기능은 회원가입이 필요없이 가능하다
    @GetMapping("/list")
    public ResponseEntity<?> list(){
        List<Item> list = itemService.findAll();

        List<ItemListDto> resultList = list.stream().map(i ->{
          try{
              return new ItemListDto(i.getItemName(),i.getPrice(),i.getStockQuantity());
          } catch (Exception e){
              e.printStackTrace();
          }
          return null;

        }).toList();
        return new ResponseEntity<>(resultList,HttpStatus.OK);
    }
    @PostMapping("/bringItem")
    ItemFeignResponse findItemById(@RequestBody FindItemDto findItemDto){

        Optional<Item> result = itemService.findOne(findItemDto.getItemId());
        Item findItem = result.get();
        return new ItemFeignResponse(findItem.getItemId(),
                findItem.getItemName(), findItem.getPrice(),
                findItem.getStockQuantity(),
                findItem.getDetail(),
                findItem.getItemState(),
                findItem.getCreatedAt(),
                findItem.getModifiedAt());
    }
    @GetMapping("/detail/{itemId}")
    public ResponseEntity<?> productDetail(@PathVariable("itemId") Long itemId){
        Optional<Item> result = itemService.findOne(itemId);
        Item item = result.get();
        ItemDetailDto itemDetailDto = new ItemDetailDto(
                item.getItemName(),
                item.getPrice(),
                item.getStockQuantity(),
                item.getItemState(),
                item.getDetail()
                );
        return new ResponseEntity<>(itemDetailDto, HttpStatus.OK);

    }

    //재고 처리하는 부분
    //order에서 주문을 하면 여기로 가져와서 redis삭제함. 그리고 일정시간 지나면 db로 반영 해줄거임.
    //itemid, count 그리고 plus가 true면 재고에 더해주는거 false면 뺴주는거.
    @PostMapping("/manage")
    public ResponseEntity<?> updateStock(@RequestBody UpdateStockDto updateStockDto){
        itemService.updateStock(updateStockDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}