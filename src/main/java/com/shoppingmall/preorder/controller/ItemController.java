package com.shoppingmall.preorder.controller;

import com.shoppingmall.preorder.domain.Item;
import com.shoppingmall.preorder.jwt.JwtFilter;
import com.shoppingmall.preorder.jwt.TokenProvider;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.*;
import com.shoppingmall.preorder.repository.ItemRepository;
import com.shoppingmall.preorder.service.UserService;
import com.shoppingmall.preorder.shopinfo.ShopInfoSearch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final ShopInfoSearch shopInfoSearch;
    private final ItemRepository itemRepository;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    @GetMapping("/list")
    public ResponseEntity<TokenDto> getShoppingInfo() {

        logger.info("여기 itemController데 되고 있는거 맞지?????");

            String result = shopInfoSearch.search();

        List<Item> list = shopInfoSearch.fromJSONtoItems(result);
        itemRepository.saveAll(list);
        return new ResponseEntity<>(HttpStatus.OK);

    }


}