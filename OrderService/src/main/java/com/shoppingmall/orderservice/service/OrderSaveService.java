package com.shoppingmall.orderservice.service;

import com.shoppingmall.orderservice.domain.Order;
import com.shoppingmall.orderservice.domain.OrderItem;
import com.shoppingmall.orderservice.repository.OrderItemRepository;
import com.shoppingmall.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderSaveService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    void saveAllOrders(List<Order> orders){
        orderRepository.saveAll(orders);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void saveOrdersNOrderItem(Order order, OrderItem orderItem){
        orderRepository.save(order);
        orderItemRepository.save(orderItem);
    }
}
