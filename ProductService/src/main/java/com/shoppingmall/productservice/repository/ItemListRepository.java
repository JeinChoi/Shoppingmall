package com.shoppingmall.productservice.repository;

import com.shoppingmall.productservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemListRepository extends JpaRepository<Item,Long> {
}
