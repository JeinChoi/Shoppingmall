package com.shoppingmall.preorder.controller;

import com.shoppingmall.preorder.domain.Item;
import com.shoppingmall.preorder.domain.OrderItem;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.domain.WishItem;
import com.shoppingmall.preorder.dto.OrderDto;
import com.shoppingmall.preorder.dto.OrderItemDto;
import com.shoppingmall.preorder.dto.WishItemDto;
import com.shoppingmall.preorder.dto.WishListDto;
import com.shoppingmall.preorder.jwt.TokenProvider;
import com.shoppingmall.preorder.repository.ItemListRepository;
import com.shoppingmall.preorder.service.*;
import com.shoppingmall.preorder.shopinfo.ShopInfoSearch;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wish")
@RequiredArgsConstructor
public class WishController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private final ShopInfoSearch shopInfoSearch;
    private final ItemListRepository itemListRepository;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final WishItemService wishItemService;
    private final ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @PostMapping("/add/wishItem")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> wishItem(@RequestBody WishItemDto wishItemDto){
        //토큰 검사 및 dto에 상품 id, 개수, 주소, 전화번호 필요
        User findUser = userService.findUser(wishItemDto.getUserId());
        Item findItem = itemService.findOne(wishItemDto.getItemId());
        //주문상품 생성
        WishItem wishItem = WishItem.createWishItem(findUser,findItem, findItem.getPrice(), wishItemDto.getCount());

        wishItemService.save(wishItem);

        return new ResponseEntity<>(wishItem.getWishItemId(),HttpStatus.OK);
    }

    @GetMapping("/list")
    public List<WishItem> wishlist(@RequestBody WishListDto wishListDto){

        return  wishItemService.findWishItemList(wishListDto.getUserId());
    }
}
