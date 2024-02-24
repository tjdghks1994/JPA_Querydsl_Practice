package com.puj.domain.comment.repository.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SearchCommentResp {
    private final Long commentId;
    private final String commentContent;
    private final LocalDateTime createdAt;
    private final Long parentCommentId;

    @Builder
    public SearchCommentResp(Long commentId, String commentContent, LocalDateTime createdAt, Long parentCommentId) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.createdAt = createdAt;
        this.parentCommentId = parentCommentId;
    }
}
