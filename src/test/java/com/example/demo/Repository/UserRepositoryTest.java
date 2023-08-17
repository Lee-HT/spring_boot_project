package com.example.demo.Repository;

import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {

    private final UserRepository userRepository;
    private static List<UserEntity> users;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeAll
    static void setUsers() {
        users = new ArrayList<>();
        users.add(UserEntity.builder().username("user1").email("email1@gmail.com")
                .roles("ROLE_USER").build());
        users.add(UserEntity.builder().username("user2").email("email2@gmail.com")
                .roles("ROLE_USER").build());
    }

    @BeforeEach
    public void saveUsers() {
        userRepository.saveAll(users);
    }

    @Test
    public void findAll() {
        List<UserEntity> users = userRepository.findAll();
    }

    @Test
    public void findByUsername() {
        String username = "name1";
        List<UserEntity> users = userRepository.findByUsername(username);
    }

    @Test
    public void saveAll() {
        List<UserEntity> newUsers = new ArrayList<>();
        newUsers.add(UserEntity.builder().username("user3").email("email3@gmail.com")
                .roles("ROLE_USER").build());
        newUsers.add(UserEntity.builder().username("user4").email("email4@gmail.com")
                .roles("ROLE_USER").build());
        userRepository.saveAll(newUsers);
    }

    @Test
    public void deleteAll() {
        userRepository.deleteAll(users);
    }

}