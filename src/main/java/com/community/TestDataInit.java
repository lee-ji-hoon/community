package com.community;

import com.community.account.entity.Account;
import com.community.account.AccountRepository;
import com.community.board.entity.Board;
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
        String BOARD_CONTENT_VALUE = "<p><b>이지훈</b><span style=\"font-family: -apple-system, BlinkMacSystemFont, &quot;Noto Sans KR&quot;, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, &quot;Noto Sans&quot;, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 1rem;\">은 바보다</span></p><p><span style=\"font-family: -apple-system, BlinkMacSystemFont, &quot;Noto Sans KR&quot;, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, &quot;Noto Sans&quot;, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 1rem; background-color: rgb(255, 255, 0);\"><font color=\"#000000\"><i><b>이오&nbsp;</b>밖에 모르는 바보..</i></font></span></p><p><span style=\"font-family: -apple-system, BlinkMacSystemFont, &quot;Noto Sans KR&quot;, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, &quot;Noto Sans&quot;, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 1rem;\"><font color=\"#000000\" style=\"\"><b style=\"\">이오는 바보다.</b></font></span></p><p><span style=\"font-family: -apple-system, BlinkMacSystemFont, &quot;Noto Sans KR&quot;, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, &quot;Noto Sans&quot;, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 1rem; background-color: rgb(255, 255, 0);\"><font color=\"#000000\" style=\"\"><i><b>츄르</b> 밖에 모르는 바보..</i></font></span></p><p><br></p>";
        boardRepository.save(new Board(null, "자유게시판", "자유게시판 제목", BOARD_CONTENT_VALUE, "tester", 1L, 0, LocalDateTime.now().minusHours(1), null));
         boardRepository.save(new Board(null, "정보공유게시판", "정보공유게시판 제목", BOARD_CONTENT_VALUE, "tester", 1L, 0, LocalDateTime.now(), null));
         boardRepository.save(new Board(null, "신입생게시판", "신입생게시판 제목", BOARD_CONTENT_VALUE, "ezhoon", 2L, 0, LocalDateTime.now().minusMinutes(20), null));
         boardRepository.save(new Board(null, "졸업생게시판", "졸업생게시판 제목", BOARD_CONTENT_VALUE, "ezhoon", 2L, 0, LocalDateTime.now().minusSeconds(40), null));

        if (!accountRepository.existsByStudentId("17-100000")) {
            accountRepository.save(new Account(null, "test@naver.com", "tester", "17-100000", "사람", passwordEncoder.encode("test1234!"),
                    true, "asdf", LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    null, null, null, null, null, null, true, true, true,
                    true, true, true, null, null));
        }

        if (!accountRepository.existsByStudentId("17-100424")) {
            accountRepository.save(new Account(null, "dlwlgns1240@naver.com", "ezhoon", "17-100424", "이지훈", passwordEncoder.encode("12401240"),
                    true, "asdf1", LocalDateTime.now().minusHours(1), LocalDateTime.now().minusHours(1),
                    "잘 부탁드립니다", "https://github.com/lee-ji-hoon", "대학생", "서울, 용산", null, null, true, true,
                    true, true, true, true, null, null));
        }
    }
}