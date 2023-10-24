package com.example.demo.Service;

import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Entity.UserEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentPageDto getCommentPage(Long pid, Pageable pageable);
    CommentDto saveComment(CommentDto commentDto);
    List<CommentLikeDto> getCommentLikeCid(Long cid,Boolean likes);
    List<CommentLikeDto> getCommentLikeUid(Long uid,Boolean likes);


}
