package com.shoppingmall.preorder.controller;

import com.shoppingmall.preorder.config.JwtFilter;
import com.shoppingmall.preorder.config.SecurityUtil;
import com.shoppingmall.preorder.config.TokenProvider;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.ChangeAddressNPhoneDto;
import com.shoppingmall.preorder.dto.ChangepwDto;
import com.shoppingmall.preorder.dto.LoginDto;
import com.shoppingmall.preorder.dto.TokenDto;
import com.shoppingmall.preorder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 해당 객체를 SecurityContextHolder에 저장하고
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        // response header에 jwt token에 넣어줌
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // tokenDto를 이용해 response body에도 넣어서 리턴
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
    //이메일 인증 : 처음에 이메일만 입력해서 인증받는다고 가정하자
    //여기서 이메일이 이미 있는지에 대한 유무도 다 정해야하다
    ///userservice에 있는 예외처리문을 여기서 써주기.


    //주소, 전화번호 업데이트
    @PatchMapping("/change_addressNphonenumber")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String changeAddressNPhoneNumber(@RequestBody ChangeAddressNPhoneDto changeAddressNPhoneDto){
       // logger.info("user 정보 ::::: {}",user.toString());
        if(userService.changeAddressNPhoneNumber(changeAddressNPhoneDto).isEmpty())
            return "해당 유저 없음";
        else return "주소와 전화번호 수정완료됨";
    }

    //비밀번호 업데이트
//    @PatchMapping("/change_password")
//    public boolean changePassword(@Validated @RequestBody ChangepwDto changepwDto){
//
//
//    }
}