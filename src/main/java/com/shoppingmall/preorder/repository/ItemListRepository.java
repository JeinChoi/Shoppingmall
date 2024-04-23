package com.shoppingmall.preorder.repository;

import com.shoppingmall.preorder.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemListRepository extends JpaRepository<Item, Long> {
}
