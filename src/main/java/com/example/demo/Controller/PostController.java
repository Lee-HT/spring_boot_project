package com.example.demo.Controller;

import com.example.demo.DTO.PageDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Service.PostService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/page")
    public PageDto getPost(
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        PageDto posts = postService.findPost(pageable);
        return posts;
    }

    @PostMapping("/save")
    public String savePost(@RequestBody PostDto postDto) {
        PostDto post = postService.savePost(postDto);
        return post.getTitle();
    }

    @GetMapping("/search/{title}")
    public List<PostEntity> searchPost(@PathVariable("title") String title,
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        postService.findPostByTitle(title,pageable);
        return null;
    }


}
