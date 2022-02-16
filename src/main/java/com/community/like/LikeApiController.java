package com.community.like;

import com.community.account.entity.Account;
import com.community.board.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LikeApiController {
    private final LikeService likeService;
    private final LikeRepository likeRepository;

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

    public void cancelLike(Account account, Board board) {
        likeRepository.deleteByAccountAndBoard(account, board);
    }
}
