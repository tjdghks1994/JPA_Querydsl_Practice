package com.puj.domain.attachfile;

import com.puj.domain.board.Board;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Table(name = "ATTACH_TB")
@ToString(exclude = "board")
@NoArgsConstructor(access = PROTECTED)
public class AttachFile {

    @Id
    @Column(name = "attach_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "attach_path")
    private String attachPath;      // 첨부파일 저장 경로

    @Column(name = "attach_filename")
    private String saveFilename;    // 첨부파일 저장 명

    @Column(name = "origin_filename")
    private String originFilename;  // 첨부파일 실제 명

    @Column(name = "attach_extension")
    private String attachExtension; // 첨부파일 확장자

    @Column(name = "image_yn")
    private String imageYN;       // 이미지 파일 여부

    @Column(name = "delete_yn")
    @ColumnDefault(value = "N")
    private String deleteYN;    // 삭제 여부

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;        // 게시글

    @PrePersist
    protected void fieldDefaultValueSetting() {
        this.deleteYN = this.deleteYN == null ? "N" : this.deleteYN;
    }

    @Builder
    public AttachFile(String attachPath, String saveFilename, String originFilename,
                 String attachExtension, String imageYN, String deleteYN, Board board) {
        this.attachPath = attachPath;
        this.saveFilename = saveFilename;
        this.originFilename = originFilename;
        this.attachExtension = attachExtension;
        this.imageYN = imageYN;
        this.deleteYN = deleteYN;
        this.board = board;
    }
}
