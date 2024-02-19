package com.puj.domain.board.repository;

import com.puj.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA 사용
public interface BoardRepository extends JpaRepository<Board, Long> {
}
