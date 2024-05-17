package com.shoppingmall.userservice;

import com.shoppingmall.userservice.domain.User;
import com.shoppingmall.userservice.dto.UserDto;
import com.shoppingmall.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {
    @Autowired
    UserService userSerivce;
    @Test
    public void createUser() throws Exception {
        for(long i=1;i<=10000;i++) {
            UserDto userDto = new UserDto("user"+i, "123412314", "010-1234-1234", "sdf@naver.com", "서울", "로", "1244-12", "token");
            User user = userSerivce.signup(userDto);
        }
    }

}
