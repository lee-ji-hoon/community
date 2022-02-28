package com.community.like;

import com.community.account.entity.Account;
import com.community.account.repository.AccountRepository;
import com.community.board.entity.Board;
import com.community.board.repository.BoardRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
