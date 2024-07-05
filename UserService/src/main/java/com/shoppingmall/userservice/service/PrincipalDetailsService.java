package com.shoppingmall.userservice.service;

import com.shoppingmall.userservice.PrincipalDetails;

import com.shoppingmall.userservice.domain.Member;
import com.shoppingmall.userservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

// formLogin 꺼놔서, http://localhost:8080/login 요청이 올 때 이 PrincipalDetailsService가 동작한다!
@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));

    }

    private UserDetails createUserDetails(Member member) {

        GrantedAuthority grandAuthority = new SimpleGrantedAuthority(member.getRole());
        log.info("createUserDetails 함수가 실행이 됐는지");
        return new User(
                String.valueOf(member.getMemberId()),
                member.getPassword(),
                Collections.singleton(grandAuthority)
        );

    }
}