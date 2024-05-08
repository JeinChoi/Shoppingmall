package com.shoppingmall.orderservice.controller;

import com.shoppingmall.orderservice.dto.*;
import com.shoppingmall.orderservice.service.OrderService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
        private final OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/order")
    public ResponseEntity<?> orderItem(@RequestBody OrderItemDto orderItemDto){
        //토큰 검사 및 dto에 상품 id, 개수, 주소, 전화번호 필요

        orderService.order(orderItemDto);
        //orderService.save(order);

        return new ResponseEntity<>(orderItemDto,HttpStatus.OK);
    }

    @PostMapping("/order/wishlist")
    public ResponseEntity<?> orderWishlist(@RequestBody OrderWishListDto orderWishListDto){
        orderService.orderWishList(orderWishListDto);

        return new ResponseEntity<>(orderWishListDto,HttpStatus.OK);
    }

    @GetMapping("/orderlist")
    public List<OrderListDto> orderList(@RequestBody FindOrderListDto findOrderListDto){
        return orderService.findOrderList(findOrderListDto.getUserId());
    }
//어떤 상품을 주문 했는지도 알려줘야 한다 어떤 아이템을 얼마로 주문 했는지
}