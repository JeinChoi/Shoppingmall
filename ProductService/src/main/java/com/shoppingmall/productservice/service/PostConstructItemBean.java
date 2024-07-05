package com.shoppingmall.productservice.service;

import com.shoppingmall.productservice.domain.Item;
import com.shoppingmall.productservice.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class PostConstructItemBean {

    private final ItemRepository itemRepository;
    private final RedisService redisService;

    @PostConstruct
    public void init(){
        //redis에 put value
        List<Item> itemList = itemRepository.findAll();
        for(int i=0;i<itemList.size();i++){
            Item item = itemList.get(i);
            redisService.setValues(item.getItemId().toString(),
                    String.valueOf(item.getStockQuantity()));
        }
    }
}
