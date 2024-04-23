package com.shoppingmall.preorder.service;

import com.shoppingmall.preorder.config.SecurityUtil;
import com.shoppingmall.preorder.domain.Authority;
import com.shoppingmall.preorder.domain.Item;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.ChangeAddressNPhoneDto;
import com.shoppingmall.preorder.dto.ChangePasswordDto;
import com.shoppingmall.preorder.dto.UserDto;
import com.shoppingmall.preorder.repository.ItemRepository;
import com.shoppingmall.preorder.repository.UserRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
    @Transactional
    public Item findOne(Long itemId) {
       return itemRepository.findOne(itemId);
    }

    @Transactional
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

}
