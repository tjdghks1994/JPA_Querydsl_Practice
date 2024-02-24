package com.puj.domain.attachfile.repository;

import com.puj.domain.attachfile.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Spring Data JPA 사용
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    @Query("select a from AttachFile a join a.board b where b.id = :boardId")
    public List<AttachFile> findAttachFileByBoardId(@Param("boardId") Long boardId);
}
