package com.shoppingmall.productservice;

import com.shoppingmall.productservice.domain.Item;
import com.shoppingmall.productservice.domain.ItemState;
import com.shoppingmall.productservice.repository.ItemRepository;
import com.shoppingmall.productservice.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ProductServiceApplicationTests {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Test
    @Transactional
    public void makeItems() {
        List<Item> items = new ArrayList<>();
        for(int i=1;i<=10000;i++){
            Item item =Item.builder().
                    itemName("item"+i).
                    price(i*100).
                    stockQuantity(100).
                    detail("item"+i).
                    itemState(ItemState.ON_SALE).build();
            items.add(item);
        }
        itemService.saveAll(items);
    }

}
