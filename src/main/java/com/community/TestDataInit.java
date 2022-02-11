package com.community;

import com.community.account.Account;
import com.community.account.AccountRepository;
import com.community.board.Board;
import com.community.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        if (!accountRepository.existsByNickname("test@naver.com")) {
            boardRepository.save(new Board(null, "title", "content", "writer", LocalDateTime.now().minusHours(1)));
            accountRepository.save(new Account(null, "test@naver.com", "tester", "17-100000", "사람", passwordEncoder.encode("test1234!"), true, "asdf", LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1), null, null, null, null, null));
        }
    }
}
