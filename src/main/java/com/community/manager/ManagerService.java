package com.community.manager;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.study.Study;
import com.community.domain.study.StudyRepository;
import com.community.service.AccountService;
import com.community.service.BoardService;
import com.community.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ManagerService {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final StudyRepository studyRepository;
    private final StudyService studyService;

    public LocalDate todayDate() {
        return LocalDate.now();
    }

    public List<Board> reportBoardLists() {
        return boardRepository.findByIsReportedOrderByUploadTimeDesc(true);
    }

    public List<Account> accountList() {
        return accountRepository.findAll();
    }

    public Map<LocalDate, Double> todayBoardAndStudyLists() {
        Map<LocalDate, Double> dateMap = new HashMap<>();
        Double todaySumBoardAndStudy = null;
        Double yesterdaySumBoardAndStudy = null;
        // 오늘 올라온 글 & 스터디
        List<Board> allBoard = boardRepository.findAll();
        List<Board> todayBoards= new ArrayList<>();
        List<Board> yesterdayBoards = new ArrayList<>();
        for (Board board : allBoard) {
            if (board.getUploadTime().toLocalDate().equals(LocalDate.now())) {
                todayBoards.add(board);
            }
            if (board.getUploadTime().toLocalDate().equals(LocalDate.now().minusDays(1))) {
                yesterdayBoards.add(board);
            }
        }
        List<Study> allStudy = studyRepository.findAll();
        List<Study> todayStudy = new ArrayList<>();
        List<Study> yesterdayStudy = new ArrayList<>();
        for (Study study : allStudy) {
            if (study.getPublishedDateTime().toLocalDate().equals(LocalDate.now())) {
                todayStudy.add(study);
            }
            if (study.getPublishedDateTime().toLocalDate().equals(LocalDate.now().minusDays(1))) {
                yesterdayStudy.add(study);
            }
        }
        todaySumBoardAndStudy = (double) (todayBoards.size() + todayStudy.size());
        log.info("todaySumBoardAndStudy={}", todaySumBoardAndStudy);
        yesterdaySumBoardAndStudy = (double) (yesterdayBoards.size() + yesterdayStudy.size());
        log.info("yesterdaySumBoardAndStudy={}", yesterdaySumBoardAndStudy);
        dateMap.put(LocalDate.now(), todaySumBoardAndStudy);
        dateMap.put(LocalDate.now().minusDays(1), yesterdaySumBoardAndStudy);
        return dateMap;
    }
}
