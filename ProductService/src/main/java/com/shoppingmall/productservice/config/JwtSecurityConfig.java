//package com.shoppingmall.productservice.config;
//
//import com.shoppingmall.productservice.jwt.JwtFilter;
//import com.shoppingmall.productservice.jwt.TokenProvider;
//import com.shoppingmall.productservice.service.RedisService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@RequiredArgsConstructor
//public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//    private final TokenProvider tokenProvider;
//    private final RedisService redisService;
//    @Override
//    public void configure(HttpSecurity http) {
//
//        // security 로직에 JwtFilter 등록
//        http.addFilterBefore(
//                new JwtFilter(tokenProvider,redisService),
//                UsernamePasswordAuthenticationFilter.class
//        );
//    }
//}