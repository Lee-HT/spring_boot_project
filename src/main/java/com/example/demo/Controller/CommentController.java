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
@RequestMapping
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("post/{pid}/comments")
    public CommentPageDto getCommentsByPost(@PathVariable Long pid,
            @PageableDefault(page = 0, size = 10, sort = "createAt", direction = Direction.DESC) Pageable pageable) {
        CommentPageDto commentPageDto = commentService.getPostCommentPage(pid, pageable);
        return commentPageDto;
    }

    @GetMapping("user/{uid}/comments")
    public CommentPageDto getCommentsByUser(@PathVariable Long uid,
            @PageableDefault(page = 0, size = 10, sort = "createAt", direction = Direction.DESC) Pageable pageable) {
        CommentPageDto commentPageDto = commentService.getUserCommentPage(uid, pageable);
        return commentPageDto;
    }

    @PostMapping("comment")
    public CommentDto saveComment(@RequestBody CommentDto commentDto) {
        CommentDto comment = commentService.saveComment(commentDto);
        return comment;
    }

    @GetMapping("user/{uid}/comment-likes")
    public List<CommentLikeDto> getCommentLikeByUser(@PathVariable Long uid, boolean likes) {
        List<CommentLikeDto> dtos = commentService.getCommentLikeUid(uid,likes);
        return dtos;
    }

    @GetMapping("comment/{cid}/comment-likes")
    public List<CommentLikeDto> getCommentLikeByComment(@PathVariable Long cid, boolean likes) {
        List<CommentLikeDto> dtos = commentService.getCommentLikeCid(cid,likes);
        return dtos;
    }
}
