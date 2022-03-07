package com.community.board.entity;

import com.community.account.entity.Account;
import com.community.forum.Forum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "rid")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long rid;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_bid")
    private Board board;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Account account;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_fid")
    private Forum forum;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime uploadTime;

    private Boolean isReported;

    private Integer reportCount;

}
