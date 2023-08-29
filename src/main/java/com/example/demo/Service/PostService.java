package com.example.demo.Service;

import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.PostEntity;
import java.util.List;

public interface PostService {

    List<PostEntity> findPostByTitle(String title);
    List<PostEntity> findPostByUsername(String username);

    PostEntity savePost(PostDto postDto);

    PostEntity updatePost(PostDto postDto);

    int deletePosts(List<Long> pid);
}
