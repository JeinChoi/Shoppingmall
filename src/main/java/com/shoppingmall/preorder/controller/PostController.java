package com.shoppingmall.preorder.controller;

import com.shoppingmall.preorder.config.SecurityConfig;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.UserDto;
import com.shoppingmall.preorder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api")
//public class PostController {
//    private final UserService userService;
//    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);
//    //기획에 없는 기능인데 토큰으로 작동하는 방법을 몰라서 테스트하기 위한 기능이다
////    @PostMapping("/post")
////    public ResponseEntity<User> signup(
////            @Valid @RequestBody PostDto postDto
////    ) {
////        return ResponseEntity.ok(userService.signup(userDto));
////    }
//
//    @GetMapping("/user")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    public ResponseEntity<User> getMyUserInfo() {
//        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
//    }
//
//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
//        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
//    }
//}