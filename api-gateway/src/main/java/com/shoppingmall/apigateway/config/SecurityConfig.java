//package com.shoppingmall.apigateway.config;
//
//import com.shoppingmall.apigateway.RedisUtil;
//import com.shoppingmall.apigateway.filter.AuthorizationHeaderFilter;
//import com.shoppingmall.apigateway.filter.GlobalFilter;
//import com.shoppingmall.apigateway.security.JwtUtil;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
//import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
//import org.springframework.security.web.server.context.ServerSecurityContextRepository;
//import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.cors.reactive.CorsConfigurationSource;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.Arrays;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration // IoC 빈(bean)을 등록
//@EnableWebFluxSecurity // 필터 체인 관리 시작 어노테이션
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final RedisUtil redisUtil;
//    private final JwtUtil jwtUtil;
//
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(auth -> auth
//                        .anyExchange().permitAll())
//                        //.pathMatchers("/error").permitAll())
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
//                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) //session STATELESS
////                .addFilterAt(new GlobalFilter(),GlobalFilter.class)
//        ;
//                //.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated());
//                return http.build();
//
////cors 설정이 안됐거나 form login 이 생성이 되서 모든 요청이 권한이 생겼거나
//    }
//
//
//}