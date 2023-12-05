package com.example.demo.Service;

import com.example.demo.DTO.LikeDto;
import com.example.demo.DTO.PostLikeDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.PostDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostPageDto findPostPage(Pageable pageable);
    PostDto findPost(Long pid);

    PostPageDto findPostByTitle(String title, Pageable pageable);

    PostPageDto findPostByUsername(String username, Pageable pageable);

    PostDto savePost(PostDto postDto);

    PostDto updatePost(PostDto postDto);

    int deletePosts(List<Long> pid);

    LikeDto getLike(Long pid, Long uid);

    LikeDto likeState(PostLikeDto dto);

    int deleteLike(Long pid, Long uid);

}
