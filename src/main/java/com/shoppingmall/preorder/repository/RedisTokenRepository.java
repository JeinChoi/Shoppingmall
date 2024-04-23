package com.shoppingmall.preorder.repository;

import com.shoppingmall.preorder.redisEntity.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTokenRepository extends CrudRepository<UserToken, Long> {
}
