package com.shoppingmall.productservice.config;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
@EnableCaching
public class RedisConfig {

    private final RedisProperties redisProperties;
    private RedissonClient redissonClient;
    // lettuce
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    // Redis template

    @Primary
    public RedisTemplate<String,String> redisTemplate() {
        RedisTemplate<String, String> storageTemplate = new RedisTemplate<>();

        storageTemplate.setConnectionFactory(redisConnectionFactory());   //connection
        storageTemplate.setKeySerializer(new StringRedisSerializer());    // key
        storageTemplate.setValueSerializer(new StringRedisSerializer());  // value
        ValueOperations<String, String> valueOperations = storageTemplate.opsForValue();
       // valueOperations.set("first","logout");
        return storageTemplate;
    }
}

