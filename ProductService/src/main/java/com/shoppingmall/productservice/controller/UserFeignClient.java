package com.shoppingmall.productservice.controller;


import com.shoppingmall.productservice.dto.FindUserDto;
import com.shoppingmall.productservice.dto.feignClientDto.UserFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="user-service")
public interface UserFeignClient {
    @PostMapping(path="/bringUser")
    UserFeignResponse findUserByLoginId(@RequestBody FindUserDto findUserDto);

}
