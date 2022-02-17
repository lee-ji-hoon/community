package com.community;

import com.community.account.entity.Account;
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
         boardRepository.save(new Board(null, "자유게시판", "자게", "자게 내용", "tester", 1L, 0, LocalDateTime.now().minusHours(1), null));
         boardRepository.save(new Board(null, "정보공유게시판", "정공게", "정공게 내용", "tester", 1L, 0, LocalDateTime.now().minusHours(1), null));
         boardRepository.save(new Board(null, "신입생게시판", "신게", "신게 내용", "1240", 2L, 0, LocalDateTime.now().minusHours(1), null));
         boardRepository.save(new Board(null, "졸업생게시판", "졸게", "졸게 내용", "1240", 2L, 0, LocalDateTime.now().minusHours(1), null));

        if (!accountRepository.existsByStudentId("17-100000")) {
            accountRepository.save(new Account(null, "test@naver.com", "tester", "17-100000", "사람", passwordEncoder.encode("test1234!"),
                    true, "asdf", LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    null, null, null, null, null, null, true, true,
                    true, true, true, true, null));
        }

        if (!accountRepository.existsByStudentId("17-100001")) {
            accountRepository.save(new Account(null, "1240@naver.com", "1240", "17-100001", "사람사람", passwordEncoder.encode("12401240"),
                    true, "asdf1", LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    null, null, null, null, null, null, true, true,
                    true, true, true, true, null));
        }
    }
}