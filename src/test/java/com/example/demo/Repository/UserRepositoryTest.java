package com.example.demo.Repository;

import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
// @TestInstance(Lifecycle.PER_CLASS)
// 초기화 문제

class UserRepositoryTest {

    private final UserRepository userRepository;
    private List<UserEntity> users = new ArrayList<>();
    private final Pageable pageable = PageRequest.of(0, 3, Direction.ASC, "uid");
    private List<Long> pk = new ArrayList<>();

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUsers() {
        for (int i = 1; i < 2; i++) {
            users.add(UserEntity.builder().username("user" + i).email("email" + i + "@gmail.com")
                    .provider("google_" + i).roles("ROLE_USER").build());
        }
        userRepository.saveAll(users);

        for (UserEntity ett : userRepository.findAll()) {
            this.pk.add(ett.getUid());
        }
    }

    @Test
    @DisplayName("전체 SELECT")
    public void findAll() {
        System.out.println("======== findAll ========");
        List<UserEntity> users = userRepository.findAll();

        System.out.println(users);

        Assertions.assertThat(users).usingRecursiveComparison().isEqualTo(this.users);
    }

    @Test
    @DisplayName("UID 기준 SELECT")
    public void FindByUid() {
        System.out.println("======== findByUsername ========");
        Optional<UserEntity> user = userRepository.findByUid(pk.get(0));

        System.out.println(user);

        Assertions.assertThat(user).usingRecursiveComparison()
                .isEqualTo(Optional.of(this.users.get(0)));
    }

    @Test
    @DisplayName("PROVIDER 기준 SELECT")
    public void findByProvider() {
        System.out.println("======== findByProvider ========");
        String provider = "google_1";
        Optional<UserEntity> user = userRepository.findByProvider(provider);

        System.out.println(user);

        Assertions.assertThat(user).usingRecursiveComparison().isEqualTo(Optional.of(this.users.get(0)));
    }

    @Test
    @DisplayName("LIKE USERNAME SELECT")
    public void findByUsernameContaining() {
        System.out.println("======== findByUsername ========");
        String username = "user";
        Page<UserEntity> pages = new PageImpl<>(this.users, this.pageable, this.users.size());
        Page<UserEntity> result = userRepository.findByUsernameContaining(username, this.pageable);

        System.out.println(result);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(pages);
    }

    @Test
    @DisplayName("INSERT")
    public void saveAll() {
        System.out.println("======== saveAll ========");
        List<UserEntity> newUsers = new ArrayList<>();
        for (int i = 3; i < 5; i++) {
            newUsers.add(UserEntity.builder().username("user" + i)
                    .email("email" + i + "@gmail.com")
                    .roles("ROLE_USER").provider("google_" + i).build());
        }
        List<UserEntity> result = userRepository.saveAll(newUsers);

        System.out.println(result);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(newUsers);
    }

}