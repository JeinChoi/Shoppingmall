package com.shoppingmall.productservice.service;

import com.shoppingmall.productservice.domain.Item;
import com.shoppingmall.productservice.dto.ManageProductDto;
import com.shoppingmall.productservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final RedisService redisService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ItemService.class);
    @Transactional
    public Optional<Item> findOne(Long itemId) {
       return itemRepository.findById(itemId);
    }

    @Transactional
    public void updateState(Long itemId){
        Item findItem = itemRepository.findById(itemId).get();
        findItem.updateStateToSoldout();

    }
    @Transactional
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

//    @Transactional
//    public void manageItem(ManageProductDto manageProductDto){
//        int left = Integer.parseInt(redisService.getValues(manageProductDto.getItemId()+""));
//        if (left==0){
//            decrementInDB(manageProductDto);
//        }else decrementInRedis(manageProductDto,left);
//    }
//
//    private void decrementInDB(ManageProductDto manageProductDto){
//
//    }
//    private void decrementInRedis(ManageProductDto manageProductDto,int left){
//
//    }
}
