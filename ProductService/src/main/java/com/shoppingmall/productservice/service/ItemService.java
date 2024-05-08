package com.shoppingmall.productservice.service;

import com.shoppingmall.productservice.domain.Item;
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

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ItemService.class);
    @Transactional
    public Optional<Item> findOne(Long itemId) {
       return itemRepository.findById(itemId);
    }

    @Transactional
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

}
