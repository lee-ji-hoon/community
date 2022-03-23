package com.community.report.entity;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "report_id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long report_id;

    // 신고자
    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    // 신고된 게시글 저장
    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_bid")
    private Board board;

    @Column(columnDefinition = "TEXT")
    private String report_title;

    // 신고 내용
    @Column(columnDefinition = "TEXT")
    private String report_content;

    private LocalDateTime reportTime;
}
