package com.community.like;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.forum.Forum;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(targetEntity = Board.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_bid")
    private Board board;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Account account;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_fid")
    private Forum forum;

    public Likes(Board board, Account account) {
        this.board = board;
        this.account = account;
    }

    public Likes(Forum forum, Account account) {
        this.forum = forum;
        this.account = account;
    }
}
