package com.puj.domain.comment.repository;

import com.puj.domain.board.Board;
import com.puj.domain.board.BoardType;
import com.puj.domain.board.repository.BoardRepository;
import com.puj.domain.comment.Comment;
import com.puj.domain.member.Member;
import com.puj.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;

    @Test
    void findCommentByBoardId() {
        Member member = Member.builder()
                .email("test@naver.com")
                .pwd("askdfeizxoci23lrn13lksl")
                .nickname("하디")
                .build();
        Board newBoard = Board.builder()
                .boardTitle("게시글 제목")
                .boardContent("게시글 내용은 무수히 많은 문자열이 작성될 수 있습니다.")
                .boardType(BoardType.NORMAL)
                .member(member)
                .deleteYN("Y")
                .parentBoard(null)
                .build();
        Comment newComment1 = Comment.builder()
                .content("댓글 내용1")
                .board(newBoard)
                .member(member)
                .parentComment(null)
                .build();
        Comment newComment2 = Comment.builder()
                .content("답글")
                .board(newBoard)
                .member(member)
                .parentComment(newComment1)
                .build();

        Member saveMember = memberRepository.save(member);
        Board saveBoard = boardRepository.save(newBoard);
        Comment saveComment1 = commentRepository.save(newComment1);
        Comment saveComment2 = commentRepository.save(newComment2);

        List<Comment> commentList = commentRepository.findCommentByBoardId(saveBoard.getId());

        assertThat(commentList.size()).isEqualTo(2);
    }

}