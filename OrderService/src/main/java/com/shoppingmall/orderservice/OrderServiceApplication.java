package com.shoppingmall.orderservice;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@EnableDiscoveryClient //이 둘의 차이
@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
//    @Bean
//    public RedissonClient redissonClient(){
//
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://192.168.0.20:6379");
//        return Redisson.create(config);
//
//    }
}
