package com.example.demo.Controller;

import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Service.CommentService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{pid}")
    public CommentPageDto getCommentsByPost(@PathVariable Long pid,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable) {
        return commentService.getCommentByPost(pid, pageable);
    }

    @GetMapping("/user/{uid}")
    public CommentPageDto getCommentsByUser(@PathVariable Long uid,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        return commentService.getCommentByUser(uid, pageable);
    }

    @PostMapping("")
    public CommentDto saveComment(@RequestBody CommentDto commentDto) {
        return commentService.saveComment(commentDto);
    }

    @GetMapping("/user/{uid}/likes/{likes}")
    public List<CommentLikeDto> getCommentLikeByUser(@PathVariable Long uid, @PathVariable boolean likes) {
        return commentService.getCommentLikeUid(uid,likes);
    }

    @GetMapping("/{cid}/likes/{likes}")
    public List<CommentLikeDto> getCommentLikeByComment(@PathVariable Long cid, @PathVariable boolean likes) {
        return commentService.getCommentLikeCid(cid,likes);
    }
}
