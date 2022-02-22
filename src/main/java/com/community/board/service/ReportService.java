package com.community.board.service;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.entity.Report;
import com.community.board.form.ReportForm;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.board.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    public void saveBoardReport(Board board, Account account, ReportForm reportForm) {
        Report report = Report.builder()
                .board(board)
                .account(account)
                .reply(null)
                .reportTime(LocalDateTime.now())
                .report_content(reportForm.getReport_content())
                .build();
        reportRepository.save(report);
        board.setUpdatableBoard(false);
        board.setRemovableBoard(false);
        boardRepository.save(board);
    }
    public void saveReplyReport(Reply reply, Account account, ReportForm reportForm) {
        Report report = Report.builder()
                .board(null)
                .account(account)
                .reply(reply)
                .reportTime(LocalDateTime.now())
                .report_content(reportForm.getReport_content())
                .build();
        reportRepository.save(report);
        reply.setReport(true);
        replyRepository.save(reply);
    }
}
