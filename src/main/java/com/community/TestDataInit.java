package com.community;

import com.community.account.entity.Account;
import com.community.account.repository.AccountRepository;
import com.community.board.entity.Board;
import com.community.board.repository.BoardRepository;
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
        String BOARD_CONTENT_VALUE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,\n" +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                "Ut enim ad minim veniam,\n" +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
                "Duis aute irure dolor in\n" +
                "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\n" +
                "Excepteur sint occaecat cupidatat non proident,\n" +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.";
        String BOARD_CONTENT_VALUE2 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,\n" +
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                "Ut enim ad minim veniam,\n" +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
                "Duis aute irure dolor in\n" +
                "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\n" +
                "Excepteur sint occaecat cupidatat non proident,\n" +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.";

        if (!accountRepository.existsByStudentId("17-100000")) {
            accountRepository.save(new Account(null, "test@naver.com", "tester", "17-100000", "사람", passwordEncoder.encode("test1234!"),
                    true, "asdf", LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1), null,
                    null, null, null, null, null, null, true, true, true,
                    true, true, true, null));
            boardRepository.save(new Board(null, "신입생게시판", "신입생게시판 제목",null, null, false,0,BOARD_CONTENT_VALUE, "tester", 1L, 0, LocalDateTime.now().minusMinutes(20), null));
            boardRepository.save(new Board(null, "졸업생게시판", "졸업생게시판 제목",null, null, false,0,BOARD_CONTENT_VALUE, "tester", 1L, 0, LocalDateTime.now().minusSeconds(40), null));
            boardRepository.save(new Board(null, "졸업생게시판", "신고된 졸업 게시글",null, null, true,5,BOARD_CONTENT_VALUE, "tester", 1L, 0, LocalDateTime.now().minusSeconds(40), null));

        }

        if (!accountRepository.existsByStudentId("17-100424")) {
            accountRepository.save(new Account(null, "dlwlgns1240@naver.com", "ezhoon", "17-100424", "이지훈", passwordEncoder.encode("12401240"),
                    true, "asdf1", LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1), null,
                    "잘 부탁드립니다", "https://github.com/lee-ji-hoon", "대학생", "서울, 용산", null, null, true, true,
                    true, true, true, true, null));
            boardRepository.save(new Board(null, "자유게시판", "자유게시판 제목",null, null, false, 0,BOARD_CONTENT_VALUE, "ezhoon", 2L, 0, LocalDateTime.now().minusHours(1), null));
        }

        if (!accountRepository.existsByStudentId("17-100444")) {
            accountRepository.save(new Account(null, "tester0@naver.com", "jwhy", "17-100444", "준영", passwordEncoder.encode("test1234!"),
                    true, "asdf12", LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1), null,
                    "hello", "https://github.com/Jwhyee", "대학생", "경기도, 수원", null, null, true, true,
                    true, true, true, true, null));
            boardRepository.save(new Board(null, "자유게시판", "자유게시판 제목",null, null, false, 0,BOARD_CONTENT_VALUE2, "jwhy", 3L, 0, LocalDateTime.now().minusHours(1), null));
        }
    }
}