package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
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
class CommentRepositoryTest {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private List<CommentEntity> comments = new ArrayList<>();
    private UserEntity uid1;

    @Autowired
    public CommentRepositoryTest(CommentRepository commentRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.uid1 = UserEntity.builder().username("user1").email("email1@gmail.com").build();
        setObjects();
    }

    void setObjects() {
        comments.add(CommentEntity.builder().uid(uid1).contents("contents1").build());
        comments.add(CommentEntity.builder().uid(uid1).contents("contents2").build());
    }

    @BeforeEach
    void setComments() {
        userRepository.save(uid1);
        commentRepository.saveAll(comments);
    }

    @Test
    public void findAll() {
        List<CommentEntity> comments = commentRepository.findAll();

        System.out.println("======== findAll ========");
        System.out.println(comments);
    }

    @Test
    public void findByUid() {
        UserEntity uid = UserEntity.builder().uid(1L).username("user").email("email@gmail.com")
                .build();
        List<CommentEntity> comments = commentRepository.findByUid(uid);

        System.out.println("======== findByUid ========");
        System.out.println(comments);
    }

    @Test
    public void saveAll() {
        List<CommentEntity> newComments = new ArrayList<>();
        UserEntity uid2 = UserEntity.builder().username("user2").email("email2@gmail.com").build();
        newComments.add(CommentEntity.builder().uid(uid2).contents("contents3")
                .build());
        newComments.add(CommentEntity.builder().uid(uid2).contents("contents4")
                .build());
        userRepository.save(uid2);
        commentRepository.saveAll(newComments);
        List<CommentEntity> comments = commentRepository.findAll();

        System.out.println("======== saveAll ========");
        System.out.println(comments);
    }

}