package com.shoppingmall.orderservice;

import com.shoppingmall.orderservice.domain.DeliveryStatus;
import com.shoppingmall.orderservice.domain.Order;
import com.shoppingmall.orderservice.domain.OrderItem;
import com.shoppingmall.orderservice.domain.OrderStatus;
import com.shoppingmall.orderservice.dto.OrderItemDto;
import com.shoppingmall.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceApplicationTests {

    @Autowired
     OrderService orderService;
    @Test
    void createOrder(){
        for(int i=0;i<100;i++){
            orderService.order(new OrderItemDto(1,126,2));
           }
    }

}
