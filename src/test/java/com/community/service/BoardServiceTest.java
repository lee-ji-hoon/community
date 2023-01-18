package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void testCall() {
        boardRepository.findById(6);
    }

    @Test
    @Order(2)
    @DisplayName("findBoardByAccount")
    void findBoardByAccount() {
        Optional<Account> byId = accountRepository.findById(1L);
        List<Board> boards = boardRepository.findByWriter(byId.get());
        assertThat(boards.size()).isEqualTo(2);
    }

    @Test
    @Order(1)
    @DisplayName("findBoardByAccountId")
    void findBoardByAccountId() {
        Optional<Account> byId = accountRepository.findById(1L);
        List<Board> boards = boardRepository.findByWriterId(byId.get().getId());
        assertThat(boards.size()).isEqualTo(2);
    }
}