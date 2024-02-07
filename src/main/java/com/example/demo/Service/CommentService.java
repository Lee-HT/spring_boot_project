package com.example.demo.Service;

import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.DTO.CommentPageDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentPageDto getCommentByPost(Long pid, Pageable pageable);
    CommentPageDto getCommentByUser(Long uid, Pageable pageable);
    List<CommentLikeDto> getCommentLikeCid(Long cid);
    List<CommentLikeDto> getCommentLikeUid(Long uid);
    Long getCountCommentLike(Long cid);
    Long saveComment(CommentDto commentDto);
    Long updateComment(CommentDto commentDto);
    Integer saveCommentLike(CommentLikeDto commentLikeDto);
    Long deleteComment(Long cid);
    Long deleteCommentLike(Long cid);



}
