package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.council.CouncilRepository;
import com.community.domain.likes.Likes;
import com.community.domain.market.MarketRepository;
import com.community.domain.study.StudyRepository;
import com.community.infra.alarm.LikeCreatePublish;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookmarkController {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;
    private final StudyRepository studyRepository;
    private final MarketRepository marketRepository;
    private final CouncilRepository councilRepository;

    // 좋아요 관련 내용
    @ResponseBody
    @RequestMapping(value = "/bookmark/add")
    public int addLikeLink(@RequestParam("like_boardId") Long like_boardId,
                           @RequestParam("like_accountId") Long like_accountId){
        log.info("좋아요 호출");
        Board board = boardRepository.findByBid(like_boardId);
        Optional<Account> findAccount = accountRepository.findById(like_accountId);
        if (findAccount.isPresent()) {
            String accountEmail = findAccount.get().getEmail();
            Account account = accountRepository.findByEmail(accountEmail);
            Account writer = board.getWriter();
            Likes likes = addLike(account, like_boardId);

            if(!writer.getId().equals(findAccount.get().getId())) {
                log.info("board 좋아요 알림 이벤트 실행");
                applicationEventPublisher.publishEvent(new LikeCreatePublish(likes, account));
            }
        }
        List<Likes> likes = likeRepository.findAllByBoard(board);
        int like_size = likes.size();

        return like_size;
    }
}
