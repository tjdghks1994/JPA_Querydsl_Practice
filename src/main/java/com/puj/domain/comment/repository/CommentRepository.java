package com.puj.domain.comment.repository;

import com.puj.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Spring Data JPA 사용
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join c.board b where b.id = :boardId")
    public List<Comment> findCommentByBoardId(@Param("boardId") Long boardId);

}
