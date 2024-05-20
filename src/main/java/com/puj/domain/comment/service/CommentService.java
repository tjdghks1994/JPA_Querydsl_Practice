package com.puj.domain.comment.service;

import com.puj.domain.comment.Comment;
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
    public List<Comment> searchCommentList(Long boardId) {
        List<Comment> commentList = repository.findCommentByBoardId(boardId);

        return commentList;
    }
}
