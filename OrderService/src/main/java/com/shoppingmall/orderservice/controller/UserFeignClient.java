package com.shoppingmall.orderservice.controller;


import com.shoppingmall.orderservice.dto.FindUserDto;
import com.shoppingmall.orderservice.dto.feignClientDto.UserFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(name="member-service", url="http://localhost:8084")
public interface UserFeignClient {
    @PostMapping(path="/user/bringUser")
    UserFeignResponse findUserByLoginId(@RequestBody FindUserDto findUserDto);
}
