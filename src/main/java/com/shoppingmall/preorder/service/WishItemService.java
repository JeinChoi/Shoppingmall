package com.shoppingmall.preorder.service;


import com.shoppingmall.preorder.domain.*;
import com.shoppingmall.preorder.dto.OrderDto;
import com.shoppingmall.preorder.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishItemService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final WishItemRepository wishItemRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
    @Transactional
    public void save(WishItem wishItem) {
        wishItemRepository.save(wishItem);
    }

    @Transactional
    public List<WishItem> findWishItemList(long userId){
        User findUser = userRepository.findOne(userId);
        return wishItemRepository.findAllByUser(findUser);

    }

}