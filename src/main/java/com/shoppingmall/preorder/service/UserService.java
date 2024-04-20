package com.shoppingmall.preorder.service;

import com.shoppingmall.preorder.config.SecurityUtil;
import com.shoppingmall.preorder.domain.Authority;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.ChangeAddressNPhoneDto;
import com.shoppingmall.preorder.dto.ChangePasswordDto;
import com.shoppingmall.preorder.dto.UserDto;
import com.shoppingmall.preorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }//이메일이 이미 있다면 예외처리

        //


        // 가입되어 있지 않은 회원이면,
        // 권한 정보 만들고
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        // 유저 정보를 만들어서 save
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phoneNumber(userDto.getPhoneNumber())
                .email(userDto.getEmail())
                .city(userDto.getCity())
                .street(userDto.getStreet())
                .zipcode(userDto.getZipcode())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    // 유저,권한 정보를 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    // 현재 securityContext에 저장된 username의 정보만 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

    @Transactional
    public Optional<User> changeAddressNPhoneNumber(ChangeAddressNPhoneDto changeAddressNPhoneDto){
        Optional<User> temp = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesByUsername);
        if(temp.isPresent()){
            userRepository.updateAddressNPhone(changeAddressNPhoneDto,temp.get().getUserId());
        }
        return temp;

    }

    @Transactional
    public Optional<User> changePassword(ChangePasswordDto changePasswordDto){
        Optional<User> temp = SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesByUsername);
        if(temp.isPresent()){
            userRepository.updatePassword(changePasswordDto,temp.get().getUserId());
        }
        return temp;

    }

}
