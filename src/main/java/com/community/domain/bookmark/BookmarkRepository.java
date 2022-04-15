package com.community.domain.bookmark;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import com.community.domain.council.Council;
import com.community.domain.market.Market;
import com.community.domain.study.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByAccountAndBoard(Account account, Board board);

    Optional<Bookmark> findByAccountAndCouncil(Account account, Council council);

    Optional<Bookmark> findByAccountAndMarket(Account account, Market market);

    Optional<Bookmark> findByAccountAndStudy(Account account, Study study);
}
