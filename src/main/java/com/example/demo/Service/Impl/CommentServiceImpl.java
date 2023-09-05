package com.example.demo.Service.Impl;

import com.example.demo.Converter.CommentConverter;
import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentConverter commentConverter,
            PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.postRepository = postRepository;
    }

    @Override
    public CommentPageDto getCommentPage(Long pid, Pageable pageable) {
        PostEntity postEntity = postRepository.findByPid(pid);
        Page<CommentEntity> comments = commentRepository.findByPid(postEntity,pageable);
        CommentPageDto commentPageDto = commentConverter.toDto(comments);

        return commentPageDto;
    }

    @Override
    public CommentDto saveComment(CommentDto commentDto, UserEntity user) {
        CommentEntity comment = commentRepository.save(commentConverter.toEntity(commentDto,user));

        return commentConverter.toDto(comment);
    }

}
