package com.example.demo.Service.Impl;

import com.example.demo.Converter.PostConverter;
import com.example.demo.DTO.LikeDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostLikeDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.PostLikeEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.PostLikeRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.PostService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostConverter postConverter;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
            PostConverter postConverter,
            UserRepository userRepository,
            PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.postConverter = postConverter;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
    }

    private String GetProvider() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    @Override
    public PostPageDto findPostPage(Pageable pageable) {
        return postConverter.toDto(postRepository.findAll(pageable));
    }

    @Override
    public PostDto findPost(Long pid) {
        PostEntity postEntity = postRepository.findByPid(pid)
                .orElseGet(() -> PostEntity.builder().build());
        return postConverter.toDto(postEntity);
    }

    @Override
    public PostPageDto findPostByTitle(String title, Pageable pageable) {
        return postConverter.toDto(postRepository.findByTitleContaining(title, pageable));
    }

    @Override
    public PostPageDto findPostByUsername(String username, Pageable pageable) {
        return postConverter.toDto(postRepository.findByUsernameContaining(username, pageable));
    }

    @Override
    public PostDto savePost(PostDto postDto) {
        Optional<UserEntity> userEntity = GetUserProv();

        if (userEntity.isPresent()) {
            PostEntity post = postConverter.toEntity(postDto, userEntity.get());
            return postConverter.toDto(postRepository.save(post));
        } else {
            return PostDto.builder().build();
        }
    }

    @Override
    public PostDto updatePost(PostDto postDto) {
        Optional<PostEntity> postEntity = postRepository.findByPid(postDto.getPid());

        if (postEntity.isPresent() && EqualUid(postEntity.get())) {
            postEntity.get()
                    .updatePost(postDto.getTitle(), postDto.getContents(), postDto.getCategory());
            return postConverter.toDto(postEntity.get());
        } else {
            return PostDto.builder().build();
        }
    }

    @Override
    public Long deletePost(Long pid) {
        Optional<PostEntity> postEntity = postRepository.findByPid(pid);
        if (postEntity.isPresent() && EqualUid(postEntity.get())) {
            postRepository.delete(postEntity.get());
            return pid;
        }
        return 0L;
    }

    @Override
    public int deletePosts(List<Long> pid) {
        Optional<UserEntity> userEntity = GetUserProv();
        List<PostEntity> posts = new ArrayList<>();
        try {
            for (Long i : pid) {
                PostEntity postEntity = GetPost(i);
                posts.add(postEntity);
                if (userEntity.isEmpty() || !Objects.equals(postEntity.getUid(),
                        userEntity.get())) {
                    return 0;
                }
            }
            postRepository.deleteAll(posts);
            if (posts.size() == pid.size()) {
                return pid.size();
            }
            return 0;
        } catch (Exception e) {
            log.info(e.toString());
        }
        return 0;
    }

    @Override
    public LikeDto getLike(Long pid) {
        Optional<UserEntity> userEntity = GetUserProv();
        if (userEntity.isPresent()) {
            return LikeDto.builder().likes(GetPostLike(pid, userEntity.get().getUid()).getLikes())
                    .build();
        } else {
            return LikeDto.builder().build();
        }
    }

    @Override
    public LikeDto setlikeState(PostLikeDto dto) {
        Optional<UserEntity> userEntity = GetUserProv();
        PostEntity postEntity = GetPost(dto.getPid());
        if (userEntity.isEmpty() || postEntity.getPid() == null) {
            return LikeDto.builder().build();
        }
        Optional<PostLikeEntity> postLike = postLikeRepository.findByPidAndUid(postEntity,
                userEntity.get());
        if (postLike.isEmpty()) {
            postLikeRepository.save(
                    PostLikeEntity.builder().pid(postEntity).uid(userEntity.get())
                            .likes(dto.getLikes())
                            .build());
        } else {
            postLike.get().updateLikes(dto.getLikes());
        }
        return LikeDto.builder().likes(dto.getLikes()).build();
    }

    @Override
    public int deleteLike(Long pid) {
        Optional<UserEntity> userEntity = GetUserProv();
        if (userEntity.isPresent()) {
            postLikeRepository.deleteByPidAndUid(GetPost(pid), userEntity.get());
            return 1;
        } else {
            return 0;
        }
    }

    // get PostLikeEntity
    private PostLikeEntity GetPostLike(Long pid, Long uid) {
        return postLikeRepository.findByPidAndUid(GetPost(pid),
                GetUserUid(uid)).orElseGet(() -> PostLikeEntity.builder().build());
    }

    private PostEntity GetPost(Long pid) {
        return postRepository.findByPid(pid).orElseGet(() -> PostEntity.builder().build());
    }

    private UserEntity GetUserUid(Long uid) {
        return userRepository.findByUid(uid).orElseGet(() -> UserEntity.builder().build());
    }

    private Optional<UserEntity> GetUserProv() {
        return userRepository.findByProvider(GetProvider());
    }

    private Boolean EqualUid(PostEntity postEntity) {
        Optional<UserEntity> userEntity = GetUserProv();
        return userEntity.isPresent() && Objects.equals(userEntity.get(),
                postEntity.getUid());
    }
}