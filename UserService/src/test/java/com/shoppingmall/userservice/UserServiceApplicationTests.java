package com.shoppingmall.userservice;

import com.shoppingmall.userservice.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {

    @Test
    public void createUser(){
        User user3 = new User(3L,"user3","123412314","010-1234-1234",true,"sdf@naver.com","서울","로","1244-12","token");
        User user4 = new User(4L,"user4","123412314","010-1234-1234",true,"sdf@naver.com","서울","로","1244-12","token");
        User user5 = new User(5L,"user5","123412314","010-1234-1234",true,"sdf@naver.com","서울","로","1244-12","token");
        User user6 = new User(6L,"user6","123412314","010-1234-1234",true,"sdf@naver.com","서울","로","1244-12","token");
        User user7 = new User(7L,"user7","123412314","010-1234-1234",true,"sdf@naver.com","서울","로","1244-12","token");
        User user8 = new User(8L,"user8","123412314","010-1234-1234",true,"sdf@naver.com","서울","로","1244-12","token");

    }

}
