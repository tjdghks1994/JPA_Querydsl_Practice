package com.puj.domain.comment.repository;

import com.puj.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA 사용
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
