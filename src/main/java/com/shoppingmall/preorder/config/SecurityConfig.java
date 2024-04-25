package com.shoppingmall.preorder.config;

import com.shoppingmall.preorder.jwt.JwtAccessDeniedHandler;
import com.shoppingmall.preorder.jwt.JwtAuthenticationEntryPoint;
import com.shoppingmall.preorder.jwt.TokenProvider;
import com.shoppingmall.preorder.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final RedisService redisService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    // PasswordEncoder는 BCryptPasswordEncoder를 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // enable h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
                .requestMatchers("/auth/login").permitAll() // 로그인 api
                .requestMatchers("/auth/signup").permitAll() // 회원가입 api
                .requestMatchers("/favicon.ico").permitAll()
                .requestMatchers("/auth/verify").permitAll()
                .requestMatchers("/items/listdata").permitAll()
                .requestMatchers("/items/list").permitAll()
                .requestMatchers("/items/detail/**").permitAll()
                .anyRequest().authenticated() // 그 외 인증 없이 접근X

                .and()
                .apply(new JwtSecurityConfig(tokenProvider,redisService)); // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용

        logger.info("securityconfig파일??? ");
        return httpSecurity.build();
    }

}