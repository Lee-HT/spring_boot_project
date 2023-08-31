package com.example.demo.Service;

import com.example.demo.DTO.PageDto;
import com.example.demo.DTO.PostDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PageDto findPost(Pageable pageable);

    PageDto findPostByTitle(String title,Pageable pageable);

    PageDto findPostByUsername(String username,Pageable pageable);

    PostDto savePost(PostDto postDto);

    PostDto updatePost(PostDto postDto);

    int deletePosts(List<Long> pid);
}
