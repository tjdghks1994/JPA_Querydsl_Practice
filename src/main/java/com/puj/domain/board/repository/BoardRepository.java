package com.puj.domain.board.repository;

import com.puj.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// Spring Data JPA 사용
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b join fetch b.member m where b.id = :boardId")
    public Optional<Board> findBoardByIdWithMember(@Param("boardId") Long boardId);
}
