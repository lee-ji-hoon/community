package com.community.board.entity;

import com.community.account.entity.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "rid")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long report_id;

    // 신고자
    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Account account;

    // 신고된 게시글 저장
    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_bid")
    private Board board;

    // 신고된 댓글 저장
    @ManyToOne(targetEntity = Reply.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "rid")
    private Reply reply;

    // 신고 내용
    @Column(columnDefinition = "TEXT")
    private String report_content;

    private LocalDateTime reportTime;
}
