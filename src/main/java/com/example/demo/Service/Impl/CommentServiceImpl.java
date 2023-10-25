package com.example.demo.Service.Impl;

import com.example.demo.Converter.CommentConverter;
import com.example.demo.Converter.CommentLikeConverter;
import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.CommentLikeRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.CommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeConverter commentLikeConverter;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentConverter commentConverter,
                              PostRepository postRepository,
                              UserRepository userRepository,
            CommentLikeRepository commentLikeRepository, CommentLikeConverter commentLikeConverter) {
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.commentLikeConverter = commentLikeConverter;
    }

    @Override
    public CommentPageDto getPostCommentPage(Long pid, Pageable pageable) {
        PostEntity postEntity = postRepository.findByPid(pid);
        Page<CommentEntity> comments = commentRepository.findByPid(postEntity, pageable);
        CommentPageDto commentPageDto = commentConverter.toDto(comments);

        return commentPageDto;
    }

    @Override
    public CommentPageDto getUserCommentPage(Long uid, Pageable pageable) {
        UserEntity userEntity = userRepository.findByUid(uid);
        Page<CommentEntity> comments = commentRepository.findByUid(userEntity,pageable);
        CommentPageDto commentPageDto = commentConverter.toDto(comments);

        return commentPageDto;
    }


    @Override
    public CommentDto saveComment(CommentDto commentDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String provider = (String) authentication.getPrincipal();
            UserEntity user = userRepository.findByProvider(provider);
            PostEntity post = postRepository.findByPid(commentDto.getPid());
            CommentEntity comment = commentRepository.save(commentConverter.toEntity(commentDto, user, post));

            return commentConverter.toDto(comment);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<CommentLikeDto> getCommentLikeCid(Long cid, Boolean likes) {
        CommentEntity commentEntity = commentRepository.findByCid(cid);
        return commentLikeConverter.toDto(commentLikeRepository.findByCidAndLikes(commentEntity,likes));
    }

    @Override
    public List<CommentLikeDto> getCommentLikeUid(Long uid, Boolean likes) {
        UserEntity userEntity = userRepository.findByUid(uid);
        return commentLikeConverter.toDto(commentLikeRepository.findByUidAndLikes(userEntity,likes));
    }

}
