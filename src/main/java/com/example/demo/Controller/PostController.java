package com.example.demo.Controller;

import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.Service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public PostPageDto postPage(
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        PostPageDto posts = postService.findPost(pageable);
        return posts;
    }

    @PostMapping("")
    public Long savePost(@RequestBody PostDto postDto) {
        PostDto post = postService.savePost(postDto);
        return post.getUid();
    }

    @GetMapping("/title/{title}")
    public PostPageDto searchTitle(@PathVariable("title") String title,
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        PostPageDto posts = postService.findPostByTitle(title, pageable);
        return posts;
    }

    @GetMapping("/username/{username}")
    public String searchUsername(@PathVariable("username") String username, Model model,
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        PostPageDto posts = postService.findPostByUsername(username, pageable);
        model.addAllAttributes(posts.getAttr());
        return "main/post";
    }

    @GetMapping("/like")
    public Boolean getLike(Long pid, Long uid) {
        return postService.getLike(pid, uid);
    }

    @PostMapping("/like")
    public Boolean putLike(Long pid, Long uid, boolean likes) {
        return postService.likePost(pid, uid, likes);
    }

    @DeleteMapping("/like")
    public Boolean deleteLike(Long pid, Long uid) {
        return postService.deleteLike(pid, uid);
    }

}
