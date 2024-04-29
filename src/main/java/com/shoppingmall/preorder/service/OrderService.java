package com.shoppingmall.preorder.service;

import com.shoppingmall.preorder.domain.Item;
import com.shoppingmall.preorder.domain.Order;
import com.shoppingmall.preorder.domain.OrderItem;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.OrderDto;
import com.shoppingmall.preorder.repository.ItemRepository;
import com.shoppingmall.preorder.repository.OrderItemRepository;
import com.shoppingmall.preorder.repository.OrderRepository;
import com.shoppingmall.preorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public void save(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }

    @Transactional
    public void orderDto(OrderDto orderDto){
        User findUser = userRepository.findOne(orderDto.getUserId());
        Item findItem = itemRepository.findOne(orderDto.getItemId());
        //주문상품 생성
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(findItem,findItem.getPrice(), orderDto.getCount());
        orderItemList.add(orderItem);
        orderItemRepository.saveAll(orderItemList);
        Order order = new Order(findUser,"ON_DELIVERY",findUser.getCity(), findUser.getStreet(), findUser.getZipcode(),orderItemList);
        orderRepository.save(order);
    }

    @Transactional
    public void order(User findUser,List<OrderItem> orderItemList,Order order){
        orderItemRepository.saveAll(orderItemList);
        orderRepository.save(order);
    }
}
