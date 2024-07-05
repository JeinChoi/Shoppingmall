package com.shoppingmall.userservice.controller;

import com.shoppingmall.userservice.domain.Member;
import com.shoppingmall.userservice.domain.User;
import com.shoppingmall.userservice.dto.*;
import com.shoppingmall.userservice.dto.feignClientDto.UserFeignResponse;
import com.shoppingmall.userservice.service.UserService;
import feign.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RedisTemplate<String,String> redisTemplate;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);
    @PostMapping("/info")
    void info(){
        logger.info("--------------userController example--------------");
    }
//    @GetMapping("/userInfo")
//    public ResponseEntity<User> getMyUserInfo() {
//        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
//    }
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(userService.login(loginDto));
    }
    @PostMapping("/reissue")
    ResponseEntity<?> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(userService.reissue(tokenRequestDto));
    }
    @PostMapping("/signup")
    ResponseEntity<?> signup(@RequestBody MemberDto memberDto) throws Exception {
        return ResponseEntity.ok(userService.signup(memberDto));
    }
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String email,
                              @RequestParam String token) {
        Optional<Member> member= userService.verifyEmail(email);
        if(member.get().getEmail_authentication_token().equals(token)){
            //권한 바꾸고
            userService.updateAuthority(member.get());

            logger.info("인증이 완료 {} {}",email,token);
            return "인증완료";
        }
        //유저가 없다면
        logger.info("인증이 안됨 {} {}",email,token);
        return "인증실패";


    }
    @PostMapping("/bringUser")
    UserFeignResponse findUserByLoginId(@RequestBody FindUserDto findUserDto){
        Member findUser = userService.findUser(findUserDto.getLoginId()).get();
        UserFeignResponse userFeignResponse = new UserFeignResponse(
                findUser.getMemberId(),
                findUser.getUsername(),
                findUser.getPhoneNumber(),
                findUser.getEmail(),
                findUser.getCity(),
                findUser.getStreet(),
                findUser.getZipcode());
        return userFeignResponse;
    }
    @PostMapping("/logout")
    public ResponseEntity<?> manageToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        ValueOperations<String,String> valueOperations =redisTemplate.opsForValue();
        valueOperations.set(bearerToken.substring(7),"logout");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //주소, 전화번호 업데이트
    @PatchMapping("/change_addressNphonenumber")
    public String changeAddressNPhoneNumber(@RequestBody ChangeAddressNPhoneDto changeAddressNPhoneDto){

        if(userService.changeAddressNPhoneNumber(changeAddressNPhoneDto).isEmpty())
            return "주소와 전화번호 수정 실패";
        else return "주소와 전화번호 수정완료됨";
    }

    //비밀번호 업데이트
    @PatchMapping("/change_password")
    public String changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        // logger.info("user 정보 ::::: {}",user.toString());
        if(userService.changePassword(changePasswordDto).isEmpty())
            return "비밀번호 수정 실패";
        else return "비밀번호 수정완료됨";
    }
}