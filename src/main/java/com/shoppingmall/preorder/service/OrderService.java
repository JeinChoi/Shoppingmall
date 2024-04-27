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

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
    @Transactional
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public void save(OrderItem orderItem){
        orderRepository.saveOrderItem(orderItem);
    }

    @Transactional
    public void order(OrderDto orderDto){
        User findUser = userRepository.findOne(orderDto.getUserId());
        Item findItem = itemRepository.findOne(orderDto.getItemId());
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(findItem,findItem.getPrice(), orderDto.getCount());
        orderItemRepository.save(orderItem);
        Order order = new Order(findUser,"ON_DELIVERY",findUser.getCity(), findUser.getStreet(), findUser.getZipcode(),orderItem);
        orderRepository.save(order);

    }

}
