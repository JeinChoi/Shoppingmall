package com.shoppingmall.orderservice;

import com.shoppingmall.orderservice.domain.DeliveryStatus;
import com.shoppingmall.orderservice.domain.Order;
import com.shoppingmall.orderservice.domain.OrderItem;
import com.shoppingmall.orderservice.domain.OrderStatus;
import com.shoppingmall.orderservice.dto.OrderItemDto;
import com.shoppingmall.orderservice.service.OrderService;
import com.shoppingmall.orderservice.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class OrderServiceApplicationTests {

    @Autowired
     OrderService orderService;
    @Autowired
    RedisService redisService;
//    @Test
//    void createOrder() throws InterruptedException {
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//
//        CountDownLatch latch = new CountDownLatch(threadCount);
//        for(int i=0;i<threadCount;i++){
//            executorService.submit(()->{
//                try{
//                    orderService.manageRedis();
//
//                }finally{
//                    latch.countDown();
//                }
//            });
//           }
//        latch.await();
//
//        assertThat(redisService.getValues("126"));
//        System.out.println("잔여 쿠폰 개수 = " + redisService.getValues("126"));
//    }

}
