package com.puj.domain.comment.repository.dto;

import com.puj.domain.comment.Comment;
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
    private SearchCommentResp(Long commentId, String commentContent, LocalDateTime createdAt, Long parentCommentId) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.createdAt = createdAt;
        this.parentCommentId = parentCommentId;
    }

    // Entity 를 DTO 로 변환
    public static SearchCommentResp toDTO(Comment commentEntity) {
        return SearchCommentResp.builder()
                .commentId(commentEntity.getId())
                .commentContent(commentEntity.getContent())
                .createdAt(commentEntity.getCreatedAt())
                .parentCommentId(getParentCommentId(commentEntity))
                .build();
    }

    private static Long getParentCommentId(Comment comment) {
        if (comment.getParentComment() != null) {
            return comment.getParentComment().getId();
        }

        return null;
    }
}
