package com.shoppingmall.productservice.repository;

import com.shoppingmall.productservice.domain.Item;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<Item,Long> {
}
