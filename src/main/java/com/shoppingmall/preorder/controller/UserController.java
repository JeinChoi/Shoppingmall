package com.shoppingmall.preorder.controller;

import com.shoppingmall.preorder.config.SecurityConfig;
import com.shoppingmall.preorder.domain.User;
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
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final RedisTemplate<String,String> redisTemplate;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);
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
    @GetMapping("/user")
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

//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
//        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
//    }
}