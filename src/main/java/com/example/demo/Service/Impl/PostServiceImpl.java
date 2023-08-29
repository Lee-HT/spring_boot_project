package com.example.demo.Service.Impl;

import com.example.demo.Converter.PostConverter;
import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Mapper.PostMapper;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.PostService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostConverter postConverter;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper,
            PostConverter postConverter,
            UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.postConverter = postConverter;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<PostEntity> findPostByTitle(String title) {
        return postRepository.findByTitleContaining(title);
    }

    @Override
    @Transactional
    public List<PostEntity> findPostByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        return postRepository.findByUsername(user);
    }

    @Override
    @Transactional
    public PostEntity savePost(PostDto postDto) {
        PostEntity post = postConverter.toEntity(postDto);
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public PostEntity updatePost(PostDto postDto) {
        PostEntity post = postRepository.findByPid(postDto.getPid());
        post.updatePost(postDto.getTitle(), post.getContents());

        return postRepository.save(post);
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
}
