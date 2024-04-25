package com.shoppingmall.preorder.controller;

import com.shoppingmall.preorder.jwt.JwtFilter;
import com.shoppingmall.preorder.jwt.TokenProvider;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/login")//로그인기능. 레더스 없이 구현했으니 로그아웃은 context에서 삭제해주면 되는거 아닌가....??
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        //받은 email로 유저 정보를 가져와서 그 유저의 권한이 ROLE_USER인 경우에만 로그인 가능
        //GUEST인 경우 즉 메일 인증을 아직 받지 않은 유저의 경우 로그인이 불가하다.
        Optional<User> findUser =userService.getUserWithAuthorities(loginDto.getEmail());
        if(findUser.isEmpty()||!findUser.get().getAuthority().getAuthorityName().equals("ROLE_USER")){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

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
    @PostMapping("/signup")
    public ResponseEntity<User> signup(
            @Valid @RequestBody UserDto userDto
    ) throws Exception {
        return ResponseEntity.ok(userService.signup(userDto));
    }
    @Transactional
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String email,
                              @RequestParam String token) {
        Optional<User> user= userService.verifyEmail(email);
        if(user.get().getEmail_authentication_token().equals(token)){
            //권한 바꾸고
            user.ifPresent(u->{
                u.updateAuthorityToUser();
            });
            //token 컬럼 비워주기...??
            logger.info("인증이 완료도니ㅏ????? {} {}",email,token);
            return "인증완료";
        }
        //유저가 없다면
        logger.info("인증이 안되나????? {} {}",email,token);
        return "인증실패";


    }

    //주소, 전화번호 업데이트
    @PatchMapping("/change_addressNphonenumber")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String changeAddressNPhoneNumber(@RequestBody ChangeAddressNPhoneDto changeAddressNPhoneDto){

        if(userService.changeAddressNPhoneNumber(changeAddressNPhoneDto).isEmpty())
            return "주소와 전화번호 수정 실패";
        else return "주소와 전화번호 수정완료됨";
    }

    //비밀번호 업데이트
    @PatchMapping("/change_password")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        // logger.info("user 정보 ::::: {}",user.toString());
        if(userService.changePassword(changePasswordDto).isEmpty())
            return "비밀번호 수정 실패";
        else return "비밀번호 수정완료됨";
    }
}