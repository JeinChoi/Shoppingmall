package com.shoppingmall.userservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.userservice.domain.Member;
import com.shoppingmall.userservice.domain.User;
import com.shoppingmall.userservice.dto.LoginDto;
import com.shoppingmall.userservice.dto.TokenDto;
import com.shoppingmall.userservice.jwt.JwtTokenProvider;
import com.shoppingmall.userservice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final UserService userService;
    private final Environment env;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인을 시도할 때 가장 먼저 실행되는 함수
    // 로그인에 대해서 Request를 비교하여 인증 처리
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String ex;
        try{
            //1. Request의 값을 Object로 변경한다.
            LoginDto creds = new ObjectMapper().readValue(request.getInputStream(),LoginDto.class);
            ex = creds.getEmail();

            //3. authenticate를 통해 토큰에 대한 인증을 처리한 후 성공,실패 여부를 반환한다.
            log.info("현재 로그인 시도 중인 email {}",ex);
            //WebSecurity.configure에서 설정된 대로 loadByUserName를 통해 DB에 있는 유저를 탐색 후 패스워드를 비교하게 됨
            return getAuthenticationManager().authenticate(
                    //2. 인증 토큰 형태로 변경. ArrayList는 권한(Role) 목록
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>())
            );
        }
        catch(IOException e){
            log.info("현재 로그인 시도 중인 email ");
            throw new RuntimeException(e);
        }finally{


        }

    }


    // 인증 성공 시의 Action
    // 여기서 JWT Token을 반환하는 로직
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        String email = ((User)authentication.getPrincipal()).getEmail();
        Member userDetails = userService.getUserDetailsByEmail(email).get();
        TokenDto tokenDto = jwtTokenProvider.createJwtAccessToken(authentication);

        response.addHeader("Authorization", tokenDto.getAccessToken());

        log.debug("로그인한 유저 :::: {}",email);
    }

}
