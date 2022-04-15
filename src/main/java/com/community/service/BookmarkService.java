package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.bookmark.Bookmark;
import com.community.domain.bookmark.BookmarkRepository;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import com.community.domain.market.Market;
import com.community.domain.market.MarketRepository;
import com.community.domain.study.Study;
import com.community.domain.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;
    private final StudyRepository studyRepository;
    private final MarketRepository marketRepository;
    private final CouncilRepository councilRepository;

    public void addBookmark(Account account, String sort, Long id) {
        Bookmark bookmark = Bookmark.builder()
                .account(account)
                .build();
        switch (sort) {
            case "board":
                Board board = boardRepository.findByBid(id);
                bookmark.setBoard(board);
                bookmarkRepository.save(bookmark);
                break;
            case "council":
                Council council = councilRepository.findByCid(id);
                bookmark.setCouncil(council);
                bookmarkRepository.save(bookmark);
                break;
            case "market":
                Market market = marketRepository.findByMarketId(id);
                bookmark.setMarket(market);
                bookmarkRepository.save(bookmark);
                break;
            case "study":
                Optional<Study> study = studyRepository.findById(id);
                bookmark.setStudy(study.get());
                bookmarkRepository.save(bookmark);
                break;
        }
    }
}
