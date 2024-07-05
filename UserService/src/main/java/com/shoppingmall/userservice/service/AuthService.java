package com.shoppingmall.userservice.service;

import com.shoppingmall.userservice.domain.Authority;
import com.shoppingmall.userservice.domain.Member;
import com.shoppingmall.userservice.domain.User;
import com.shoppingmall.userservice.dto.MemberDto;
import com.shoppingmall.userservice.dto.UserDto;
import com.shoppingmall.userservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    @Transactional
    public Member signup(MemberDto memberDto) throws Exception {
        //logger.info("현재 유저가 있는지 없는지 {}",userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()));
//        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
//            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
//        }//이메일이 이미 있다면 예외처리

//
//        String receiverMail = userDto.getEmail();
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        String email_authentication_token = UUID.randomUUID().toString();
//        userDto.updateEamilAuthenticationToken(email_authentication_token);
//
//
//        message.addRecipients(MimeMessage.RecipientType.TO, receiverMail);// 보내는 대상
//        message.setSubject("Shoppingmall 회원가입 이메일 인증");// 제목
//
//        String body = "<div>"
//                + "<h1> 안녕하세요. Shoppingmall 입니다</h1>"
//                + "<br>"
//                + "<p>아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
//                + "<a href='http://localhost:9001/user/user/verify?email="+receiverMail+"&token=" + userDto.getEmail_authentication_token() + "'>인증 링크</a>"
//                + "</div>";
//
//        message.setText(body, "utf-8", "html");// 내용, charset 타입, subtype
//        // 보내는 사람의 이메일 주소, 보내는 사람 이름
//        message.setFrom(new InternetAddress("shoppingmall_auth@naver.com", "Shoppingmall"));// 보내는 사람
//        javaMailSender.send(message); // 메일 전송


        // 가입되어 있지 않은 회원이면,
        // 권한 정보 만들고
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER") //나중에 guest로 변경 하든지 권한 없애든지 하기
                .build();

        // 유저 정보를 만들어서 save
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(memberDto.getPassword())
                .phoneNumber(memberDto.getPhoneNumber())
                .email(memberDto.getEmail())
                .city(memberDto.getCity())
                .street(memberDto.getStreet())
                .zipcode(memberDto.getZipcode())
                //.authority(authority)
                .email_authentication_token(memberDto.getEmail_authentication_token())
                .activated(true)
                .build();

        return memberRepository.save(member);
    }
}
