package com.shoppingmall.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class RedisService {
//주문을 한 그 시점에서 item 재고를 빼줘야한다
    private final RedisTemplate<String, String> storageTemplate;

    public void setValues(String key,String  data) {
        ValueOperations<String, String> values = storageTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = storageTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, String> values = storageTemplate.opsForValue();
        if (values.get(key) == null) {
            return "none";
        }
        return values.get(key);
    }

    public void deleteValues(String key) {
        storageTemplate.delete(key);
    }

    public void expireValues(String key,Long timeout) {
        storageTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

//    public void setHashOps(Long key, Map<String, String> data) {
//        HashOperations<String, String, Object> values = storageTemplate.opsForHash();
//        values.putAll(key, data);
//    }
//
//    @Transactional(readOnly = true)
//    public String getHashOps(String key, String hashKey) {
//        HashOperations<String, String, Object> values = storageTemplate.opsForHash();
//        return Boolean.TRUE.equals(values.hasKey(key, hashKey)) ? (String) storageTemplate.opsForHash().get(key, hashKey) : "";
//    }
//
//    public void deleteHashOps(String key, String hashKey) {
//        HashOperations<String, String, Object> values = storageTemplate.opsForHash();
//        values.delete(key, hashKey);
//    }

    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }
}