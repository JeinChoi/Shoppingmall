package com.shoppingmall.preorder.service;

import com.shoppingmall.preorder.config.SecurityUtil;
import com.shoppingmall.preorder.domain.Authority;
import com.shoppingmall.preorder.domain.User;
import com.shoppingmall.preorder.dto.ChangeAddressNPhoneDto;
import com.shoppingmall.preorder.dto.ChangePasswordDto;
import com.shoppingmall.preorder.dto.UserDto;
import com.shoppingmall.preorder.repository.UserRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
    @Transactional
    public User signup(UserDto userDto) throws Exception {
        logger.info("현재 유저가 있는지 없는지 {}",userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()));
        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }//이메일이 이미 있다면 예외처리


        String receiverMail = userDto.getEmail();
        MimeMessage message = javaMailSender.createMimeMessage();

        String email_authentication_token = UUID.randomUUID().toString();
        userDto.updateEamilAuthenticationToken(email_authentication_token);


        message.addRecipients(MimeMessage.RecipientType.TO, receiverMail);// 보내는 대상
        message.setSubject("Artify 회원가입 이메일 인증");// 제목

        String body = "<div>"
                + "<h1> 안녕하세요. Artify 입니다</h1>"
                + "<br>"
                + "<p>아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
                + "<a href='http://localhost:8081/auth/verify?email="+receiverMail+"&token=" + userDto.getEmail_authentication_token() + "'>인증 링크</a>"
                + "</div>";

        message.setText(body, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("shoppingmall_auth@naver.com", "Auth-Admin"));// 보내는 사람
        javaMailSender.send(message); // 메일 전송


        // 가입되어 있지 않은 회원이면,
        // 권한 정보 만들고
        Authority authority = Authority.builder()
                .authorityName("ROLE_GUEST")
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
                .authority(authority)
                .email_authentication_token(userDto.getEmail_authentication_token())
                .activated(true)
                .build();

        return userRepository.save(user);
    }
    @Transactional
    public Optional<User> verifyEmail(String email){
        return userRepository.findOneWithAuthoritiesByEmail(email);
    }

    // 유저,권한 정보를 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email);
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
