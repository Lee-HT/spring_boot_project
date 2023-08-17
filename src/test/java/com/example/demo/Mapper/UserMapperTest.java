package com.example.demo.Mapper;

import com.example.demo.Domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "local")
@MybatisTest
// None == 실제 db 연결
@AutoConfigureTestDatabase(replace = Replace.NONE)
// db 롤백 여부
@Rollback(value = true)
class UserMapperTest {

    private final UserMapper userMapper;
    private static User user;

    @Autowired
    public UserMapperTest(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    // beforeAll 사용 시 static 선언 필요
    @BeforeAll
    static void user() {
        user = User.builder()
                .username("username")
                .email("email@gmail.com")
                .build();
    }
}