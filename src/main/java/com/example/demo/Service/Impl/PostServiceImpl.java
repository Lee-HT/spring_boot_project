package com.example.demo.Service.Impl;

import com.example.demo.Converter.PostConverter;
import com.example.demo.DTO.LikeDto;
import com.example.demo.DTO.PostLikeDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
    // 테스트 코드 x
    public PostPageDto findPost(Pageable pageable) {
        return postConverter.toDto(postRepository.findAll(pageable));
    }

    @Override
    // 테스트 코드 수정 필요
    public PostPageDto findPostByTitle(String title, Pageable pageable) {
        return postConverter.toDto(postRepository.findByTitleContaining(title, pageable));
    }

    @Override
    public PostPageDto findPostByUsername(String username, Pageable pageable) {
        return postConverter.toDto(postRepository.findByUsernameContaining(username, pageable));
    }

    @Override
    public PostDto savePost(PostDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String provider = (String) authentication.getPrincipal();
        System.out.println(provider);
        try{
            UserEntity user = userRepository.findByProvider(provider);
            PostEntity post = postConverter.toEntity(postDto, user);

            return postConverter.toDto(postRepository.save(post));
        }catch (Exception e){
            System.out.println(String.format("save error : %s",e));
            return null;
        }

    }

    @Override
    public PostDto updatePost(PostDto postDto) {
        PostEntity post = postRepository.findByPid(postDto.getPid());
        post.updatePost(postDto.getTitle(), postDto.getContents(), postDto.getCategory());

        return postConverter.toDto(post);
    }

    @Override
    public int deletePosts(List<Long> pid) {
        List<PostEntity> posts = new ArrayList<>();
        try {
            for (Long i : pid) {
                posts.add(getPost(i));
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
    public LikeDto getLike(Long pid, Long uid) {
        return LikeDto.builder().likes(getPostLike(pid, uid).isLikes()).build();
    }


    @Override
    public LikeDto likeState(PostLikeDto dto) {
        PostLikeEntity postLike = getPostLike(dto.getPid(), dto.getUid());
        postLike.updateLikes(dto.getLikes());

        return LikeDto.builder().likes(postLike.isLikes()).build();
    }

    @Override
    public int deleteLike(Long pid,Long uid) {
        try {
            postLikeRepository.deleteByPidAndUid(getPost(pid), getUser(uid));
            return 1;
        }catch (Exception e){
            return 0;
        }
    }

    // get PostLikeEntity
    private PostLikeEntity getPostLike(Long pid, Long uid) {
        return postLikeRepository.findByPidAndUid(getPost(pid), getUser(uid));
    }

    // get PostEntity
    private PostEntity getPost(Long pid) {
        return postRepository.findByPid(pid);
    }

    // get UserEntity
    private UserEntity getUser(Long uid) {
        return userRepository.findByUid(uid);
    }
}
