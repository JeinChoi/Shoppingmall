package com.shoppingmall.orderservice.repository;

import com.shoppingmall.orderservice.redisEntity.UserToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTokenRepository extends CrudRepository<UserToken, Long> {
}
