package com.shoppingmall.userservice;

import com.shoppingmall.userservice.domain.Member;
import com.shoppingmall.userservice.domain.User;
import com.shoppingmall.userservice.dto.MemberDto;
import com.shoppingmall.userservice.dto.UserDto;
import com.shoppingmall.userservice.repository.MemberRepository;
import com.shoppingmall.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Role;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserServiceApplicationTests {
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void createUser() throws Exception {
        List<Member> members = new ArrayList<>();
        for(long i=1;i<=10000;i++) {
            MemberDto memberDto = new MemberDto("user"+i, "123412314", "010-1234-1234", "sdf@naver.com", "서울", "로", "1244-12", "token");
            Member member = Member.builder()
                    .username(memberDto.getUsername())
                    .password(memberDto.getPassword())
                    .phoneNumber(memberDto.getPhoneNumber())
                    .email(memberDto.getEmail())
                    .city(memberDto.getCity())
                    .street(memberDto.getStreet())
                    .zipcode(memberDto.getZipcode())
                    .role("ROLE_USER")
                    .email_authentication_token(memberDto.getEmail_authentication_token())
                    .activated(true)
                    .build();
            members.add(member);

        } memberRepository.saveAll(members);
    }

}
