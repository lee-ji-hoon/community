package com.community.domain.bookmark;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import com.community.domain.council.Council;
import com.community.domain.market.Market;
import com.community.domain.study.Study;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "bookmarkId")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookmark_id;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(targetEntity = Council.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "council_id")
    private Council council;

    @ManyToOne(targetEntity = Market.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "council_id")
    private Market market;

    @ManyToOne(targetEntity = Study.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

}
