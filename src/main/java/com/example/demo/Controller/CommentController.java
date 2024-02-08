package com.example.demo.Controller;

import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Service.CommentService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{pid}")
    public ResponseEntity<CommentPageDto> getCommentsByPost(@PathVariable Long pid,
            @PageableDefault(size = 15, sort = "createdAt", direction = Direction.ASC) Pageable pageable) {
        HttpStatus status = HttpStatus.OK;
        CommentPageDto response = commentService.getCommentByPost(pid, pageable);
        if (response.getTotalPages() == null) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<CommentPageDto> getCommentsByUser(@PathVariable Long uid,
            @PageableDefault(size = 15, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        HttpStatus status = HttpStatus.OK;
        CommentPageDto response = commentService.getCommentByUser(uid, pageable);
        if (response.getTotalPages() == null) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/user/{uid}/likes")
    public ResponseEntity<List<CommentLikeDto>> getCommentLikeByUser(@PathVariable Long uid) {
        HttpStatus status = HttpStatus.OK;
        List<CommentLikeDto> response = commentService.getCommentLikeUid(uid);
        return new ResponseEntity<>(response,status);
    }

    @GetMapping("/{cid}/likes")
    public ResponseEntity<List<CommentLikeDto>> getCommentLikeByComment(@PathVariable Long cid) {
        HttpStatus status = HttpStatus.OK;
        List<CommentLikeDto> response = commentService.getCommentLikeCid(cid);
        return new ResponseEntity<>(response,status);
    }

    @GetMapping("/{cid}/user/{uid}/likes")
    public ResponseEntity<CommentLikeDto> getCommentLikeByUidCid(@PathVariable Long uid, @PathVariable Long cid){
        HttpStatus status = HttpStatus.OK;
        CommentLikeDto response = commentService.getCommentLikeByUidPid(uid,cid);
        if (response.getLikes() == null){
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<>(response,status);
    }

    @GetMapping("/{cid}/likes/count")
    public ResponseEntity<Long> getCountCommentLike(@PathVariable Long cid){
        HttpStatus status = HttpStatus.OK;
        Long response = commentService.getCountCommentLike(cid);
        return new ResponseEntity<>(response,status);
    }

    @PostMapping("")
    public ResponseEntity<Long> saveComment(@RequestBody CommentDto commentDto) {
        HttpStatus status = HttpStatus.CREATED;
        Long response = commentService.saveComment(commentDto);
        if (response == 0) {
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<>(response, status);
    }

    @PatchMapping
    public ResponseEntity<Long> updateComment(@RequestBody CommentDto commentDto) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        Long response = commentService.updateComment(commentDto);
        if (response == 0) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }

    @PutMapping("/likes")
    public ResponseEntity<Long> putCommentLike(@RequestBody CommentLikeDto commentLikeDto){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Integer response = commentService.saveCommentLike(commentLikeDto);
        if (response == 201){
            status = HttpStatus.CREATED;
        } else if (response == 204) {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/{cid}")
    public ResponseEntity<Long> deleteComment(@PathVariable Long cid) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        Long response = commentService.deleteComment(cid);
        if (response == 0) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/{cid}/likes")
    public ResponseEntity<Long> deleteCommentLike(@PathVariable Long cid){
        HttpStatus status= HttpStatus.NO_CONTENT;
        Long response = commentService.deleteCommentLike(cid);
        if (response == 0){
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }
}
