package com.example.demo.Service;

import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.PostDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostPageDto findPost(Pageable pageable);

    PostPageDto findPostByTitle(String title,Pageable pageable);

    PostPageDto findPostByUsername(String username,Pageable pageable);

    PostDto savePost(PostDto postDto);

    PostDto updatePost(PostDto postDto);

    int deletePosts(List<Long> pid);
}
