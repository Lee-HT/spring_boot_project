package com.example.demo.Service;

import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.UserEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostPageDto findPost(Pageable pageable);

    PostPageDto findPostByTitle(String title, Pageable pageable);

    PostPageDto findPostByUsername(String username, Pageable pageable);

    PostDto savePost(PostDto postDto, UserEntity user);

    PostDto updatePost(PostDto postDto);

    int deletePosts(List<Long> pid);

    boolean likePost(Long pid, Long uid);

    boolean hatePost(Long pid, Long uid);
}
