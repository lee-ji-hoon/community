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

    @Test
    @Order(1)
    @DisplayName("findBoardByAccountUsername")
    void findBoardByAccountUsername() {
        Optional<Account> byId = accountRepository.findById(1L);
        long start = System.currentTimeMillis();
        List<Board> boards = boardRepository.findByWriter_Username(byId.get().getUsername());
        long end = System.currentTimeMillis();
        System.out.println("JPA 실행 시간 : " + (end - start)/1000.0);
        assertThat(boards.size()).isEqualTo(2);
    }

    @Test
    @Order(2)
    @DisplayName("findBoardByAccount")
    void findBoardByAccount() {
        Optional<Account> byId = accountRepository.findById(1L);
        long start = System.currentTimeMillis();
        List<Board> boards = boardRepository.findByWriter(byId.get());
        long end = System.currentTimeMillis();
        System.out.println("JPA 실행 시간 : " + (end - start)/1000.0);
        assertThat(boards.size()).isEqualTo(2);
    }

    @Test
    @Order(3)
    @DisplayName("findBoardByAccountId")
    void findBoardByAccountId() {
        Optional<Account> byId = accountRepository.findById(1L);
        long start = System.currentTimeMillis();
        List<Board> boards = boardRepository.findByWriterId(byId.get().getId());
        long end = System.currentTimeMillis();
        System.out.println("JPA 실행 시간 : " + (end - start)/1000.0);
        assertThat(boards.size()).isEqualTo(2);
    }
}