package com.community.manager;

import com.community.domain.account.AccountRepository;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.service.AccountService;
import com.community.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ManagerService {
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final BoardRepository boardRepository;
    private final BoardService boardService;

    public List<Board> reportBoardLists() {
        return boardRepository.findByIsReportedOrderByUploadTimeDesc(true);
    }
}
