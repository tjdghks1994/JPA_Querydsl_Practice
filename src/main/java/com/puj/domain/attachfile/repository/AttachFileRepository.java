package com.puj.domain.attachfile.repository;

import com.puj.domain.attachfile.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// Spring Data JPA 사용
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    @Query("select a from AttachFile a join a.board b where b.id = :boardId and a.deleteYN = 'N'")
    List<AttachFile> findAttachFileByBoardId(@Param("boardId") Long boardId);

//    @Query("select a from AttachFile a join a.board b where a.id = :attachId and b.id = :boardId")
    Optional<AttachFile> findAttachFileByIdAndBoardId(@Param("attachId") Long attachId, @Param("boardId") Long boardId);

    @Modifying
    @Query("update AttachFile a set a.deleteYN = 'Y' where a.board.id = :boardId and a.id in :attachIds")
    int bulkAttachFileDelete(@Param("boardId") Long boardId, @Param("attachIds") List<Long> attachIds);
}
