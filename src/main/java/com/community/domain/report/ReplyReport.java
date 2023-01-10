package com.community.domain.report;

import com.community.domain.account.Account;
import com.community.domain.reply.Reply;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "report_id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long report_id;

    // 신고자
    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    // 신고된 댓글 저장
    @ManyToOne(targetEntity = Reply.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_rid")
    private Reply reply;

    @Column(columnDefinition = "TEXT")
    private String report_title;

    // 신고 내용
    @Column(columnDefinition = "TEXT")
    private String report_content;

    private LocalDateTime reportTime;
}
