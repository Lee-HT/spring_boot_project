package com.example.demo.Service;

import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.DTO.CommentPageDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentPageDto getCommentByPost(Long pid, Pageable pageable);
    CommentPageDto getCommentByUser(Long uid, Pageable pageable);
    List<CommentLikeDto> getCommentLikeCid(Long cid,Boolean likes);
    List<CommentLikeDto> getCommentLikeUid(Long uid,Boolean likes);
    Long saveComment(CommentDto commentDto);
    Long updateComment(CommentDto commentDto);
    Long deleteComment(Long cid);



}
