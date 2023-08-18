package com.example.demo.Service.Impl;

import com.example.demo.Mapper.PostMapper;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper){
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

}
