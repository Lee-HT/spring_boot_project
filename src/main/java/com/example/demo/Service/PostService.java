package com.example.demo.Service;

import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostLikeDto;
import com.example.demo.DTO.PostPageDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostPageDto findPostPage(Pageable pageable);

    PostDto findPost(Long pid);
    PostPageDto findPostByCategory(String category, Pageable pageable);

    PostPageDto findPostByTitle(String title, Pageable pageable);

    PostPageDto findPostByUsername(String username, Pageable pageable);

    Map<String, Object> getLike(Long pid);

    Long getLikeCount(Long pid);

    PostDto savePost(PostDto postDto);

    PostDto updatePost(PostDto postDto);

    Map<String, Object> savelikeState(PostLikeDto dto);

    Long deletePost(Long pid);

    Integer deletePosts(List<Long> pid);

    Long deletePostLike(Long pid);

}
