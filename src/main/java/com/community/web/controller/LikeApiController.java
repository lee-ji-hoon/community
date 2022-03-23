package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.infra.alarm.LikeCreatePublish;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.likes.LikeRepository;
import com.community.domain.likes.Likes;
import com.community.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LikeApiController {
    private final LikeService likeService;

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    // 좋아요 관련 내용
    @ResponseBody
    @RequestMapping(value = "/board/detail/like")
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

    @ResponseBody
    @RequestMapping(value = "/board/detail/like-cancel")
    public int removeLikeLink(@RequestParam("like_boardId") Long like_boardId, @RequestParam("like_accountId") Long like_accountId){
        log.info("좋아요 취소 호출");
        Board board = boardRepository.findByBid(like_boardId);
        Optional<Account> findAccount = accountRepository.findById(like_accountId);
        if (findAccount.isPresent()) {
            String accountEmail = findAccount.get().getEmail();
            Account account = accountRepository.findByEmail(accountEmail);
            boolean existLike = likeRepository.existsByAccountAndBoard(account, board);
            if (existLike) {
                Likes likes = likeRepository.findByBoardAndAccount(board, account);
                log.info("likeId = " + likes);
                likeRepository.delete(likes);
                List<Likes> likesList = likeRepository.findAllByBoard(board);
                int likesSize = likesList.size();
                return likesSize;
            }
        }
        List<Likes> likesList = likeRepository.findAllByBoard(board);
        int like_size = likesList.size();
        return like_size;
    }

    public Likes addLike(Account account, Long boardId) {
        Likes likes = likeService.addLike(account, boardId);
        if (likes != null) {
            new ResponseEntity<>(HttpStatus.OK);
        } else {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return likes;
    }
}
