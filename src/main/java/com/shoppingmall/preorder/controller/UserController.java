package com.shoppingmall.preorder.controller;

import com.shoppingmall.preorder.config.SecurityConfig;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.ChangeAddressNPhoneDto;
import com.shoppingmall.preorder.dto.ChangePasswordDto;
import com.shoppingmall.preorder.dto.UserDto;
import com.shoppingmall.preorder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RedisTemplate<String,String> redisTemplate;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/userInfo")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> manageToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        ValueOperations<String,String> valueOperations =redisTemplate.opsForValue();
        valueOperations.set(bearerToken.substring(7),"logout");
        return new ResponseEntity<>(HttpStatus.OK);
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