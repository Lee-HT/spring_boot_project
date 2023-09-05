package com.example.demo.Controller;

import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public String postPage(Model model,
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        PostPageDto posts = postService.findPost(pageable);
        model.addAllAttributes(posts.getAttr());
        return "main/post";
    }

    @GetMapping("/save")
    public String uploadPost() {
        return "post/save";
    }

    @PostMapping("")
    public String savePost(@RequestBody PostDto postDto, UserEntity user) {
        PostDto post = postService.savePost(postDto,user);
        return "redirect:post";
    }

    @GetMapping("/title/{title}")
    public String searchTitle(@PathVariable("title") String title, Model model,
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        PostPageDto posts = postService.findPostByTitle(title, pageable);
        model.addAllAttributes(posts.getAttr());
        return "main/post";
    }

    @GetMapping("/username/{username}")
    public String searchUsername(@PathVariable("username") String username, Model model,
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        PostPageDto posts = postService.findPostByUsername(username,pageable);
        model.addAllAttributes(posts.getAttr());
        return "main/post";
    }

    @GetMapping("/like")
    @ResponseBody
    public Boolean likePost(Long pid, Long uid) {
        return postService.likePost(pid, uid);
    }

    @GetMapping("/hate")
    @ResponseBody
    public Boolean hatePost(Long pid, Long uid) {
        return postService.hatePost(pid, uid);
    }


}
