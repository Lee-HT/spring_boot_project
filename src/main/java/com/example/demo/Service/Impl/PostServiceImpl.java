package com.example.demo.Service.Impl;

import com.example.demo.Converter.PostConverter;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.PostLikeEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Mapper.PostMapper;
import com.example.demo.Repository.PostLikeRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.PostService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostConverter postConverter;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper,
            PostConverter postConverter,
            UserRepository userRepository,
            PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postConverter = postConverter;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Override
    @Transactional
    // 테스트 코드 x
    public PostPageDto findPost(Pageable pageable) {
        return postConverter.toDto(postRepository.findAll(pageable));
    }

    @Override
    @Transactional
    // 테스트 코드 수정 필요
    public PostPageDto findPostByTitle(String title, Pageable pageable) {
        return postConverter.toDto(postRepository.findByTitleContaining(title, pageable));
    }

    @Override
    @Transactional
    public PostPageDto findPostByUsername(String username, Pageable pageable) {
        return postConverter.toDto(postRepository.findByUsernameContaining(username, pageable));
    }

    @Override
    @Transactional
    public PostDto savePost(PostDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String provider = ((DefaultOAuth2User) authentication.getPrincipal()).getAttribute("provider");
        UserEntity user = userRepository.findByProvider(provider);

        PostEntity post = postConverter.toEntity(postDto,user);
        return postConverter.toDto(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostDto updatePost(PostDto postDto) {
        PostEntity post = postRepository.findByPid(postDto.getPid());
        post.updatePost(postDto.getTitle(), postDto.getContents(), postDto.getCategory());

        return postConverter.toDto(post);
    }

    @Override
    @Transactional
    public int deletePosts(List<Long> pid) {
        List<PostEntity> posts = new ArrayList<>();
        try {
            for (Long i : pid) {
                posts.add(postRepository.findByPid(i));
            }
            postRepository.deleteAll(posts);
            if (posts.size() == pid.size()) {
                return pid.size();
            }
            return 0;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean likePost(Long pid, Long uid) {
        PostEntity post = postRepository.findByPid(pid);
        UserEntity user = userRepository.findByUid(uid);
        PostLikeEntity postLike = postLikeRepository.findByPidAndUid(post, user);
        postLike.updateLikes(!postLike.isLikes());

        return postLike.isLikes();
    }

    @Override
    @Transactional
    public boolean hatePost(Long pid, Long uid) {
        PostEntity post = postRepository.findByPid(pid);
        UserEntity user = userRepository.findByUid(uid);
        PostLikeEntity postLike = postLikeRepository.findByPidAndUid(post, user);
        postLike.updateHate(!postLike.isHate());

        return postLike.isHate();
    }
}
