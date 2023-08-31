package com.example.demo.Repository;

import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
// 테스트 단위 인스턴스 생성 (before All static 없이 사용 가능)
@TestInstance(Lifecycle.PER_CLASS)
class UserRepositoryTest {

    private final UserRepository userRepository;
    private List<UserEntity> users = new ArrayList<>();
    private Pageable pageable = PageRequest.of(0,3, Direction.ASC,"uid");

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeAll
    void setUsers() {
        users.add(UserEntity.builder().username("user1").email("email1@gmail.com")
                .roles("ROLE_USER").build());
        users.add(UserEntity.builder().username("user2").email("email2@gmail.com")
                .roles("ROLE_USER").build());
        this.users = userRepository.saveAll(users);
    }

    @Test
    public void findAll() {
        List<UserEntity> users = userRepository.findAll();

        Assertions.assertThat(users).usingRecursiveComparison().isEqualTo(this.users);

        System.out.println("======== findAll ========");
        System.out.println(users);
    }

    @Test
    public void FindByUid() {
        UserEntity uid = userRepository.findByUid(1L);

        Assertions.assertThat(uid).usingRecursiveComparison().isEqualTo(this.users.get(0));

        System.out.println("======== findByUsername ========");
        System.out.println(uid);
    }

    @Test
    public void findByUsername() {
        String username = "user1";
        UserEntity user = userRepository.findByUsername(username);

        Assertions.assertThat(user).usingRecursiveComparison().isEqualTo(this.users.get(0));

        System.out.println("======== findByUsername ========");
        System.out.println(user);
    }

    @Test
    public void findByUsernameContaining() {
        String username = "user";
        Page<UserEntity> pages = new PageImpl<>(this.users,this.pageable,this.users.size());
        Page<UserEntity> result = userRepository.findByUsernameContaining(username,this.pageable);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(pages);

        System.out.println("======== findByUsername ========");
        System.out.println(result);
    }

    @Test
    public void saveAll() {
        List<UserEntity> newUsers = new ArrayList<>();
        newUsers.add(UserEntity.builder().username("user3").email("email3@gmail.com")
                .roles("ROLE_USER").build());
        newUsers.add(UserEntity.builder().username("user4").email("email4@gmail.com")
                .roles("ROLE_USER").build());
        List<UserEntity> result = userRepository.saveAll(newUsers);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(newUsers);

        System.out.println("======== saveAll ========");
        System.out.println(result);

    }

}