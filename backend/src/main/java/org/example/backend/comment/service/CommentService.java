package org.example.backend.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.comment.dto.request.CommentRequestDto;
import org.example.backend.comment.dto.response.CommentResponseDto;
import org.example.backend.comment.entity.Comment;
import org.example.backend.user.entity.User;
import org.example.backend.comment.repository.CommentRepository;
import org.example.backend.global.security.SecurityUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final SecurityUtils securityUtils;

    private static final int MAX_COMMENT_COUNT = 5;

    public void addComment(CommentRequestDto dto) {
        User user = getCurrentUser();

        long count = commentRepository.countByUser(user);
        if (count >= MAX_COMMENT_COUNT) {
            throw new IllegalStateException("댓글은 최대 5개까지 작성할 수 있습니다.");
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
                .map(comment -> CommentResponseDto.builder()
                        .id(comment.getId())
                        .username(comment.getUser().getUsername())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList();
    }

    public void deleteComment(Long commentId) {
        User user = getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new SecurityException("본인의 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }
}
