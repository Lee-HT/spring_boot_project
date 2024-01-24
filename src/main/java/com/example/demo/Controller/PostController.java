package com.example.demo.Controller;

import com.example.demo.DTO.LikeDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostLikeDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.Service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public PostPageDto getPostPage(
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        return postService.findPostPage(pageable);
    }

    @PostMapping("")
    public PostDto savePost(@RequestBody PostDto postDto) {
        return postService.savePost(postDto);
    }

    @GetMapping("/{pid}")
    public PostDto searchPid(@PathVariable("pid") Long pid) {
        log.info(pid.toString());
        return postService.findPost(pid);
    }

    @GetMapping("/title/{title}")
    public PostPageDto searchTitle(@PathVariable("title") String title,
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        return postService.findPostByTitle(title, pageable);
    }

    @GetMapping("/username/{username}")
    public PostPageDto searchUsername(@PathVariable("username") String username,
            @PageableDefault(page = 0, size = 10, sort = "pid", direction = Direction.DESC) Pageable pageable) {
        return postService.findPostByUsername(username, pageable);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Long> deleteByPid(@PathVariable("pid") Long pid){
        HttpStatus status = HttpStatus.NO_CONTENT;
        Long result = postService.deletePost(pid);
        if (result == 0){
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }

    // 현재 좋아요 상태
    @GetMapping("/{pid}/likes")
    public ResponseEntity<LikeDto> getLike(@PathVariable Long pid) {
        LikeDto likeDto = postService.getLike(pid);
        HttpStatus status = HttpStatus.OK;
        if (likeDto.getLikes() == null) {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(likeDto, status);
    }

    // 좋아요 or 싫어요 추가
    @PutMapping("/likes")
    public ResponseEntity<LikeDto> likeState(@RequestBody PostLikeDto dto) {
        LikeDto likeDto = postService.setlikeState(dto);
        HttpStatus status = HttpStatus.CREATED;
        if (likeDto.getLikes() == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(likeDto, status);
    }

    // 성공시 true
    @DeleteMapping("/{pid}/likes")
    public ResponseEntity<Integer> deleteLike(@PathVariable Long pid) {
        int result = postService.deleteLike(pid);
        HttpStatus status = HttpStatus.NO_CONTENT;
        if (result == 0) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }

}
