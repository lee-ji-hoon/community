package com.community.domain.board;

import com.community.domain.account.Account;
import com.community.domain.council.Council;
import com.community.domain.market.Market;
import com.community.domain.meetings.Meetings;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
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
