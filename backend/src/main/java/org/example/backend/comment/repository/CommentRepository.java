package org.example.backend.comment.repository;

import org.example.backend.comment.entity.Comment;
import org.example.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countByUser(User user);
}