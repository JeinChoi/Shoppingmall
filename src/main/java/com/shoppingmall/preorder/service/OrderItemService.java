package com.shoppingmall.preorder.service;


import com.shoppingmall.preorder.domain.Item;
import com.shoppingmall.preorder.domain.Order;
import com.shoppingmall.preorder.domain.OrderItem;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.OrderDto;
import com.shoppingmall.preorder.repository.OrderItemRepository;
import com.shoppingmall.preorder.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
    @Transactional
    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }




}
