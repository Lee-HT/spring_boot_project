package com.example.demo.Service.Impl;

import com.example.demo.Converter.PostConverter;
import com.example.demo.Converter.PostLikeConverter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
    private final PostLikeConverter postLikeConverter;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    public PostServiceImpl(PostRepository postRepository,
            PostConverter postConverter,
            PostLikeConverter postLikeConverter, UserRepository userRepository,
            PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.postConverter = postConverter;
        this.postLikeConverter = postLikeConverter;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
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
    public Map<String, Object> getLike(Long pid) {
        Optional<UserEntity> userEntity = getUserProv();
        Map<String, Object> response = new HashMap<>();
        if (userEntity.isPresent()) {
            response.put("permit", true);
            Optional<PostLikeEntity> postLikeEntity = postLikeRepository.findByPidAndUid(
                    getPost(pid),
                    userEntity.get());
            postLikeEntity.ifPresent(likeEntity -> response.put("contents",
                    LikeDto.builder().likes(likeEntity.getLikes()).build()));
        } else {
            response.put("permit", false);
        }
        return response;
    }

    @Override
    public Long getLikeCount(Long pid) {
        Optional<PostEntity> postEntity = postRepository.findByPid(pid);
        return postEntity.map(entity -> postLikeRepository.countByPidAndLikes(entity, true)).orElse(null);
    }

    @Override
    public PostDto savePost(PostDto postDto) {
        Optional<UserEntity> userEntity = getUserProv();

        if (userEntity.isPresent()) {
            postDto.setUsername(userEntity.get().getUsername());
            PostEntity post = postConverter.toEntity(postDto, userEntity.get());
            return postConverter.toDto(postRepository.save(post));
        } else {
            return PostDto.builder().build();
        }
    }

    @Override
    public Map<String, Object> savelikeState(PostLikeDto dto) {
        Map<String, Object> response = new HashMap<>();
        Optional<UserEntity> userEntity = getUserProv();
        Optional<PostEntity> postEntity = postRepository.findByPid(dto.getPid());
        if (userEntity.isPresent() && postEntity.isPresent()) {
            response.put("permit", true);
            Optional<PostLikeEntity> postLike = postLikeRepository.findByPidAndUid(postEntity.get(),
                    userEntity.get());
            if (postLike.isPresent()) {
                postLike.get().updateLikes(dto.getLikes());
                response.put("contents", LikeDto.builder().likes(dto.getLikes()).build());
            } else {
                postLikeRepository.save(
                        postLikeConverter.toEntity(dto, userEntity.get(), postEntity.get()));
            }
        } else {
            response.put("permit", false);
        }
        return response;

    }

    @Override
    public PostDto updatePost(PostDto postDto) {
        Optional<PostEntity> postEntity = postRepository.findByPid(postDto.getPid());

        if (postEntity.isPresent() && equalUid(postEntity.get())) {
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
        if (postEntity.isPresent() && equalUid(postEntity.get())) {
            postRepository.delete(postEntity.get());
            return pid;
        }
        return 0L;
    }

    @Override
    public Integer deletePosts(List<Long> pid) {
        Optional<UserEntity> userEntity = getUserProv();
        List<PostEntity> posts = new ArrayList<>();
        if (userEntity.isPresent()) {
            for (Long i : pid) {
                PostEntity postEntity = getPost(i);
                posts.add(postEntity);
                if (!Objects.equals(postEntity.getUid(), userEntity.get())) {
                    return 0;
                }
            }
            postRepository.deleteAll(posts);
            return posts.size();
        }
        return 0;
    }

    @Override
    public Long deletePostLike(Long pid) {
        Optional<UserEntity> userEntity = getUserProv();
        if (userEntity.isPresent()) {
            postLikeRepository.deleteByPidAndUid(getPost(pid), userEntity.get());
            return pid;
        } else {
            return 0L;
        }
    }

    private String getProvider() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    private PostEntity getPost(Long pid) {
        return postRepository.findByPid(pid).orElseGet(() -> PostEntity.builder().build());
    }

    private Optional<UserEntity> getUserProv() {
        return userRepository.findByProvider(getProvider());
    }

    private Boolean equalUid(PostEntity postEntity) {
        Optional<UserEntity> userEntity = getUserProv();
        return userEntity.isPresent() && Objects.equals(userEntity.get(),
                postEntity.getUid());
    }
}