package org.example.backend.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.comment.dto.request.CommentRequestDto;
import org.example.backend.comment.dto.response.CommentResponseDto;
import org.example.backend.comment.entity.Comment;
import org.example.backend.global.exception.requestError.comment.CommentMaxReachedException;
import org.example.backend.global.exception.requestError.comment.CommentNotOwnerException;
import org.example.backend.user.entity.User;
import org.example.backend.comment.repository.CommentRepository;
import org.example.backend.global.security.core.SecurityUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final SecurityUtils securityUtils;

    private static final int MAX_COMMENT_COUNT = 10;

    public void addComment(CommentRequestDto dto) {
        User user = getCurrentUser();

        long count = commentRepository.countByUser(user);
        if (count >= MAX_COMMENT_COUNT) {
            throw new CommentMaxReachedException();
        }

        Comment comment = Comment.builder()
                .user(user)
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getAllComments() {
        return commentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::toDto)
                .toList();
    }

    public void deleteComment(Long commentId) {
        User user = getCurrentUser();
        commentRepository.findById(commentId).ifPresent(comment -> {
            if (!comment.getUser().getId().equals(user.getId())) {
                throw new CommentNotOwnerException();
            }
            commentRepository.delete(comment);
        });
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }

    private CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
