package com.community.board.entity;

import com.community.account.entity.Account;
import com.community.council.Council;
import com.community.market.Market;
import com.community.study.entity.Meetings;
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

    @ManyToOne(targetEntity = Council.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "council_id")
    private Council council;

    @ManyToOne(targetEntity = Meetings.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "meetings_id")
    private Meetings meetings;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime uploadTime;

    private Boolean isReported;

    private Integer reportCount;

}
