package com.example.demo.Repository;

import com.example.demo.Entity.UserEntity;
import io.jsonwebtoken.lang.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private final List<UserEntity> users = new ArrayList<>();
    private final Pageable pageable = PageRequest.of(0, 3, Direction.ASC, "uid");
    private final List<Long> pk = new ArrayList<>();

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
    void findAll() {
        List<UserEntity> users = userRepository.findAll();

        Assertions.assertThat(users).usingRecursiveComparison().isEqualTo(this.users);
    }

    @Test
    void FindByUid() {
        Optional<UserEntity> result = userRepository.findByUid(pk.get(0));

        Assert.isTrue(result.isPresent());
        Assertions.assertThat(result.get()).usingRecursiveComparison().isEqualTo(this.users.get(0));
    }

    @Test
    void findByProvider() {
        String provider = "google_1";
        Optional<UserEntity> result = userRepository.findByProvider(provider);

        Assert.isTrue(result.isPresent());
        Assertions.assertThat(result.get()).usingRecursiveComparison().isEqualTo(this.users.get(0));
    }

    @Test
    void findByUsernameContaining() {
        String username = "user";
        Page<UserEntity> pages = new PageImpl<>(this.users, this.pageable, this.users.size());
        Page<UserEntity> result = userRepository.findByUsernameContaining(username, this.pageable);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(pages);
    }

    @Test
    void saveAll() {
        List<UserEntity> newUsers = new ArrayList<>();
        for (int i = 3; i < 5; i++) {
            newUsers.add(UserEntity.builder().username("user" + i)
                    .email("email" + i + "@gmail.com")
                    .roles("ROLE_USER").provider("google_" + i).build());
        }
        List<UserEntity> result = userRepository.saveAll(newUsers);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(newUsers);
    }

}