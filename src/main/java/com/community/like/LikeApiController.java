package com.community.like;

import com.community.account.entity.Account;
import com.community.account.repository.AccountRepository;
import com.community.board.entity.Board;
import com.community.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 좋아요 관련 내용
    @ResponseBody
    @RequestMapping(value = "/board/detail/like")
    public int addLikeLink(@RequestParam("like_boardId") Long like_boardId, @RequestParam("like_accountId") Long like_accountId){
        log.info("좋아요 호출");
        Board board = boardRepository.findByBid(like_boardId);
        Optional<Account> findAccount = accountRepository.findById(like_accountId);
        if (findAccount.isPresent()) {
            String accountEmail = findAccount.get().getEmail();
            Account account = accountRepository.findByEmail(accountEmail);
            addLike(account, like_boardId);
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

    public void addLike(Account account, Long boardId) {
        boolean result = false;
        if (account != null) {
            result = likeService.addLike(account, boardId);
        }
        if (result) {
            new ResponseEntity<>(HttpStatus.OK);
        } else {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
