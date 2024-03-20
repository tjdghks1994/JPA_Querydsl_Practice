package com.puj.domain.attachfile.service;

import com.puj.domain.attachfile.AttachFile;
import com.puj.domain.attachfile.exception.NotFoundAttachFileException;
import com.puj.domain.attachfile.repository.AttachFileRepository;
import com.puj.domain.attachfile.repository.dto.CreateAttachReq;
import com.puj.domain.attachfile.repository.dto.SearchAttachResp;
import com.puj.domain.board.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachFileService {
    private final AttachFileRepository repository;

    // 첨부파일 목록 저장
    public void saveAttachFileList(List<CreateAttachReq> attachReq, Board board) {
        attachReq.forEach(req -> {
            AttachFile attachEntity = CreateAttachReq.conversionAttachEntity(req, board);
            // 첨부파일 저장
            repository.save(attachEntity);
        });
    }

    // boardId와 연관된 첨부파일 목록 조회
    public List<AttachFile> searchAttachFileList(Long boardId) {
        List<AttachFile> attachFileList = repository.findAttachFileByBoardId(boardId);

        return attachFileList;
    }

    // 일부 첨부파일 목록 삭제 (soft delete)
    public void removeAttachFileList(List<Long> deleteAttachIds, Long boardId) {
        deleteAttachIds.stream().forEach((attachId) -> {
            repository.findAttachFileByIdAndBoardId(attachId, boardId)
                    .orElseThrow(() -> new NotFoundAttachFileException("게시글에 존재하지 않는 첨부파일 입니다."));
        });
        // 첨부파일 삭제 처리 - deleteYN 값을 Y로 변경
        repository.bulkAttachFileDelete(boardId, deleteAttachIds);
    }

    // 모든 첨부파일 목록 삭제 (soft delete)
    public void removeAllAttachFiles(Long boardId) {
        repository.bulkAttachFileAllDelete(boardId);
    }
}
