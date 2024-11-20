package com.shoppingmall.userservice.service;


import com.shoppingmall.userservice.domain.Authority;
import com.shoppingmall.userservice.domain.Member;
import com.shoppingmall.userservice.domain.RefreshToken;
import com.shoppingmall.userservice.dto.*;
import com.shoppingmall.userservice.jwt.JwtTokenProvider;
import com.shoppingmall.userservice.repository.MemberRepository;
import com.shoppingmall.userservice.repository.RefreshTokenRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);


    @Transactional
    public Member signup(MemberDto memberDto) throws Exception {
        logger.info("현재 유저가 있는지 없는지 {}",memberRepository.findByEmail(memberDto.getEmail()));
        if (memberRepository.findByEmail(memberDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }//이메일이 이미 있다면 예외처리


        String receiverMail = memberDto.getEmail();
        MimeMessage message = javaMailSender.createMimeMessage();

        String email_authentication_token = UUID.randomUUID().toString();
        memberDto.updateEamilAuthenticationToken(email_authentication_token);


        message.addRecipients(MimeMessage.RecipientType.TO, receiverMail);// 보내는 대상
        message.setSubject("Shoppingmall 회원가입 이메일 인증");// 제목

        String body = "<div>"
                + "<h1> 안녕하세요. Shoppingmall 입니다</h1>"
                + "<br>"
                + "<p>아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
                + "<a href='http://localhost:9001/user/verify?email="+receiverMail+"&token=" +
                memberDto.getEmail_authentication_token() + "'>인증 링크</a>"
                + "</div>";

        message.setText(body, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("shoppingmall_auth@naver.com", "Shoppingmall"));// 보내는 사람
        javaMailSender.send(message); // 메일 전송


        // 가입되어 있지 않은 회원이면,
        // 권한 정보 만들고
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        // 유저 정보를 만들어서 save
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .phoneNumber(memberDto.getPhoneNumber())
                .email(memberDto.getEmail())
                .city(memberDto.getCity())
                .street(memberDto.getStreet())
                .zipcode(memberDto.getZipcode())
                .role("ROLE_GUEST")
                .email_authentication_token(memberDto.getEmail_authentication_token())
                .activated(true)
                .build();

        return memberRepository.save(member);


    }
    @Transactional
    public Optional<Member> verifyEmail(String email){
        return memberRepository.findByEmail(email);
    }

//    @Transactional
//    public TokenDto login(LoginDto loginDto){
//
//        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
//        //요청을 통해 넘어온 email과 password 기반으로 UsernamePasswordAuthenticationToken을 반환한다.
//        log.info("------------login 메서드로 다시 돌아왔는지");
//
//        //loadUserByUsername 메서드 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//
//        log.info("인증 됐는지 {}",authentication.isAuthenticated());
//
//        TokenDto tokenDto = jwtTokenProvider.createJwtAccessToken(authentication);
//
//        RefreshToken refreshToken = RefreshToken.builder()
//                .key(authentication.getName())
//                .value(tokenDto.getRefreshToken())
//                .build();
//
//        refreshTokenRepository.save(refreshToken);//refreshtoken 저장.
//
//        return tokenDto;
//
//    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto){
        
        if (!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }
    @Transactional
    public void updateAuthority(Member member){
        member.updateAuthorityToUser();
    }
    // 유저,권한 정보를 가져오는 메소드
//    @Transactional(readOnly = true)
//    public Optional<User> getUserWithAuthorities(String email) {
//        return userRepository.findOneWithAuthoritiesByEmail(email);
//    }

    @Transactional(readOnly = true)
    public Optional<Member> getUserDetailsByMemberId(Long memberId) {
        return memberRepository.findByMemberId(memberId);
    }
    @Transactional
    public Optional<Member> findUser(long userId){
        return memberRepository.findById(userId);
    }

    // 현재 securityContext에 저장된 username의 정보만 가져오는 메소드
//    @Transactional(readOnly = true)
//    public Optional<User> getMyUserWithAuthorities() {
//        return SecurityUtil.getCurrentUsername()
//                .flatMap(userRepository::findOneWithAuthoritiesByUsername);
//    }

    @Transactional
    public Optional<Member> changeAddressNPhoneNumber(ChangeAddressNPhoneDto changeAddressNPhoneDto){
        Optional<Member> temp = memberRepository.findById(changeAddressNPhoneDto.getUserId());
        temp.ifPresent(member -> member.updateAddressNPhone(changeAddressNPhoneDto));
        return temp;
    }

    @Transactional
    public  Optional<Member> changePassword(ChangePasswordDto changePasswordDto){
        Optional<Member> temp = memberRepository.findById(changePasswordDto.getUserId());
        temp.ifPresent(member -> member.updatePassword(changePasswordDto.getPassword()));

        return temp;

    }

}