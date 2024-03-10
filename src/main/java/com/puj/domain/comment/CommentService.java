package com.puj.domain.comment;

import com.puj.domain.comment.repository.CommentRepository;
import com.puj.domain.comment.repository.dto.SearchCommentResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;

    // boardId와 연관된 댓글 목록 조회
    public List<SearchCommentResp> searchCommentList(Long boardId) {
        List<SearchCommentResp> commentList = repository.findCommentByBoardId(boardId)
                .stream()
                .map((comment) -> SearchCommentResp.toDTO(comment))
                .collect(Collectors.toList());

        return commentList;
    }
}
