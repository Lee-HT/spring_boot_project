package com.example.demo.Repository;

import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

@ActiveProfiles(profiles = "test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
// 테스트 단위 인스턴스 생성 (before All static 없이 사용 가능)
@TestInstance(Lifecycle.PER_CLASS)
class UserRepositoryTest {

    private final UserRepository userRepository;
    private List<UserEntity> users = new ArrayList<>();
    private Pageable pageable = PageRequest.of(0, 3, Direction.ASC, "uid");

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeAll
    void setUsers() {
        for (int i = 1; i < 2; i++) {
            System.out.println(i);
            users.add(UserEntity.builder().username("user" + i).email("email" + i + "@gmail.com")
                    .roles("ROLE_USER").build());
        }
        userRepository.saveAll(users);
    }

    @Test
    @DisplayName("전체 SELECT")
    public void findAll() {
        System.out.println("======== findAll ========");
        List<UserEntity> users = userRepository.findAll();

        Assertions.assertThat(users).usingRecursiveComparison().isEqualTo(this.users);

        System.out.println(users);
    }

    @Test
    @DisplayName("UID 기준 SELECT")
    public void FindByUid() {
        System.out.println("======== findByUsername ========");
        UserEntity uid = userRepository.findByUid(1L);

        Assertions.assertThat(uid).usingRecursiveComparison().isEqualTo(this.users.get(0));

        System.out.println(uid);
    }

    @Test
    @DisplayName("USERNAME 기준 SELECT")
    public void findByUsername() {
        System.out.println("======== findByUsername ========");
        String username = "user1";
        UserEntity user = userRepository.findByUsername(username);

        Assertions.assertThat(user).usingRecursiveComparison().isEqualTo(this.users.get(0));

        System.out.println(user);
    }

    @Test
    @DisplayName("LIKE USERNAME SELECT")
    public void findByUsernameContaining() {
        System.out.println("======== findByUsername ========");
        String username = "user";
        Page<UserEntity> pages = new PageImpl<>(this.users, this.pageable, this.users.size());
        Page<UserEntity> result = userRepository.findByUsernameContaining(username, this.pageable);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(pages);

        System.out.println(result);
    }

    @Test
    @DisplayName("INSERT")
    public void saveAll() {
        System.out.println("======== saveAll ========");
        List<UserEntity> newUsers = new ArrayList<>();
        for (int i = 3; i < 5; i++) {
            newUsers.add(UserEntity.builder().username("user" + i)
                    .email("email" + i + "@gmail.com")
                    .roles("ROLE_USER").build());
        }
        List<UserEntity> result = userRepository.saveAll(newUsers);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(newUsers);

        System.out.println(result);
    }

}