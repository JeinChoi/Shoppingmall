package com.shoppingmall.productservice.controller;

import com.shoppingmall.productservice.dto.FindUserDto;
import com.shoppingmall.productservice.dto.OrderWishListDto;
import com.shoppingmall.productservice.dto.feignClientDto.UserFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="order-service", url="http://localhost:8081")
public interface OrderFeignClient {
    @PostMapping(path="/order/wishlist")
    ResponseEntity<?> orderWishlist(@RequestBody OrderWishListDto orderWishListDto);
}
