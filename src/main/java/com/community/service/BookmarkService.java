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

    public void addBookmark(Account account, String postSort, Long postId) {
        Bookmark bookmark = Bookmark.builder()
                .account(account)
                .build();
        switch (postSort) {
            case "board":
                Board board = boardRepository.findById(postId).get();
                bookmark.setBoard(board);
                bookmarkRepository.save(bookmark);
                break;
            case "council":
                Council council = councilRepository.findByCid(postId);
                bookmark.setCouncil(council);
                bookmarkRepository.save(bookmark);
                break;
            case "market":
                Market market = marketRepository.findByMarketId(postId);
                bookmark.setMarket(market);
                bookmarkRepository.save(bookmark);
                break;
            case "study":
                Optional<Study> study = studyRepository.findById(postId);
                bookmark.setStudy(study.get());
                bookmarkRepository.save(bookmark);
                break;
        }
    }

    public void deleteBookmark(Account account, String postSort, Long postId) {
        Optional<Bookmark> currentBookmark;
        switch (postSort) {
            case "board":
                Board currentBoard = boardRepository.findById(postId).get();
                currentBookmark = bookmarkRepository.findByAccountAndBoard(account, currentBoard);
                bookmarkRepository.delete(currentBookmark.get());
                break;
            case "council":
                Council council = councilRepository.findByCid(postId);
                currentBookmark = bookmarkRepository.findByAccountAndCouncil(account, council);
                bookmarkRepository.delete(currentBookmark.get());
                break;
            case "market":
                Market market = marketRepository.findByMarketId(postId);
                currentBookmark = bookmarkRepository.findByAccountAndMarket(account, market);
                bookmarkRepository.delete(currentBookmark.get());
                break;
            case "study":
                Optional<Study> study = studyRepository.findById(postId);
                currentBookmark = bookmarkRepository.findByAccountAndStudy(account, study.get());
                bookmarkRepository.delete(currentBookmark.get());
                break;
        }
    }

    public boolean existBookmarkByBoardAndAccount(Board board, Account account) {
        return bookmarkRepository.existsByBoardAndAccount(board, account);
    }
}
