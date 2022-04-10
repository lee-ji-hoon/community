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

    public List<Board> reportBoardLists() {
        return boardRepository.findByIsReportedOrderByUploadTimeDesc(true);
    }

    public List<Account> accountList() {
        return accountRepository.findAll();
    }

    public Map<LocalDate, Integer> todayBoardAndStudyLists() {
        Map<LocalDate, Integer> dateMap = new HashMap<>();
        Integer todaySumBoardAndStudy = null;
        Integer yesterdaySumBoardAndStudy = null;
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
        todaySumBoardAndStudy += (todayBoards.size() + todayStudy.size());
        yesterdaySumBoardAndStudy += (yesterdayBoards.size() + yesterdayStudy.size());
        dateMap.put(LocalDate.now(), todaySumBoardAndStudy);
        dateMap.put(LocalDate.now().minusDays(1), yesterdaySumBoardAndStudy);
        return dateMap;
    }

    /*public void selectDate(LocalDate localDate, String sort) {
        if (sort.equals("account")) {
            List<Account> accountList = accountRepository.findAll();
            List<Account> todayAccountList = new ArrayList<>();
            List<Account> yesterdayAccountList = new ArrayList<>();
            for (Account account : accountList) {
                if (account.getJoinedAt().toLocalDate().equals(LocalDate.now())) {
                    todayAccountList.add(account);
                }
                if (account.getJoinedAt().toLocalDate().equals(LocalDate.now().minusDays(1))) {
                    yesterdayAccountList.add(account);
                }
            }
        }

        if (sort.equals("board")) {

        }

        if (sort.equals("study")) {

        }
    }
    public void daily(LocalDate localDate) {
        List<Account> accountList = accountRepository.findAll();
        List<Account> todayAccountList = new ArrayList<>();
        List<Account> yesterdayAccountList = new ArrayList<>();
        for (Account account : accountList) {
            if (account.getJoinedAt().toLocalDate().equals(LocalDate.now())) {
                todayAccountList.add(account);
            }
            if (account.getJoinedAt().toLocalDate().equals(LocalDate.now().minusDays(1))) {
                yesterdayAccountList.add(account);
            }
        }
    }*/
}
