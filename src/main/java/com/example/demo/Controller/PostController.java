package com.example.demo.Controller;

import com.example.demo.DTO.LikeDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostLikeDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.Service.PostService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public ResponseEntity<PostPageDto> getPostPage(
            @PageableDefault(sort = "pid", direction = Direction.DESC) Pageable pageable) {
        HttpStatus status = HttpStatus.OK;
        PostPageDto response = postService.findPostPage(pageable);
        if (response.getTotalPages() == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/{pid}")
    public ResponseEntity<PostDto> getPostPid(@PathVariable("pid") Long pid) {
        HttpStatus status = HttpStatus.OK;
        PostDto response = postService.findPost(pid);
        if (response.getPid() == null) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            return new ResponseEntity<>(response, status);
        }
        return new ResponseEntity<>(status);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<PostPageDto> getPostByTitle(@PathVariable("title") String title,
            @PageableDefault(sort = "pid", direction = Direction.DESC) Pageable pageable) {
        HttpStatus status = HttpStatus.OK;
        PostPageDto response = postService.findPostByTitle(title, pageable);
        if (response.getTotalPages() == null) {
            status = HttpStatus.BAD_REQUEST;
        } else if (!response.getContents().isEmpty()) {
            return new ResponseEntity<>(response, status);
        }
        return new ResponseEntity<>(status);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<PostPageDto> getPostByUsername(@PathVariable("username") String username,
            @PageableDefault(sort = "pid", direction = Direction.DESC) Pageable pageable) {
        HttpStatus status = HttpStatus.OK;
        PostPageDto response = postService.findPostByUsername(username, pageable);
        if (response.getTotalPages() == null) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            return new ResponseEntity<>(response, status);
        }
        return new ResponseEntity<>(status);
    }

    @GetMapping("/{pid}/likes")
    public ResponseEntity<LikeDto> getPostLike(@PathVariable Long pid) {
        HttpStatus status = HttpStatus.OK;
        Map<String, Object> response = postService.getLike(pid);
        if ((Boolean) response.get("permit")) {
            return new ResponseEntity<>((LikeDto) response.get("contents"), status);
        }
        status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(status);
    }

    @PostMapping("")
    public ResponseEntity<PostDto> savePost(@RequestBody PostDto postDto) {
        HttpStatus status = HttpStatus.CREATED;
        PostDto response = postService.savePost(postDto);
        if (response.getUid() == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }

    // 좋아요 or 싫어요 추가
    @PutMapping("/likes")
    public ResponseEntity<LikeDto> savePostLike(@RequestBody PostLikeDto dto) {
        Map<String, Object> response = postService.savelikeState(dto);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (((Boolean) response.get("permit"))) {
            status = HttpStatus.CREATED;
            if (response.containsKey("contents")) {
                status = HttpStatus.NO_CONTENT;
            }
        }
        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Long> deleteByPid(@PathVariable("pid") Long pid) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        if (postService.deletePost(pid) == 0) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/{pid}/likes")
    public ResponseEntity<Integer> deletePostLike(@PathVariable Long pid) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        if (postService.deletePostLike(pid) == 0) {
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(status);
    }

}
