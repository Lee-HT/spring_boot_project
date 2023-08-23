package com.example.demo.Repository;

import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
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
    private List<UserEntity> users = new ArrayList<>();

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
        setObjects();
    }

    void setObjects() {
        users.add(UserEntity.builder().username("user1").email("email1@gmail.com")
                .roles("ROLE_USER").build());
        users.add(UserEntity.builder().username("user2").email("email2@gmail.com")
                .roles("ROLE_USER").build());
    }

    @BeforeEach
    void setUsers() {
        userRepository.saveAll(users);
    }

    @Test
    public void findAll() {
        List<UserEntity> users = userRepository.findAll();

        System.out.println("======== findAll ========");
        System.out.println(users);
    }

    @Test
    public void findByUsername() {
        String username = "user1";
        List<UserEntity> users = userRepository.findByUsername(username);

        System.out.println("======== findByUsername ========");
        System.out.println(users);
    }

    @Test
    public void saveAll() {
        List<UserEntity> newUsers = new ArrayList<>();
        newUsers.add(UserEntity.builder().username("user3").email("email3@gmail.com")
                .roles("ROLE_USER").build());
        newUsers.add(UserEntity.builder().username("user4").email("email4@gmail.com")
                .roles("ROLE_USER").build());
        userRepository.saveAll(newUsers);
        List<UserEntity> users = userRepository.findAll();

        System.out.println("======== saveAll ========");
        System.out.println(users);

    }

}