package com.community.report.service;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.report.entity.BoardReport;
import com.community.report.entity.ReplyReport;
import com.community.report.form.BoardReportForm;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.report.form.ReplyReportForm;
import com.community.report.repository.BoardReportRepository;
import com.community.report.repository.ReplyReportRepository;
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

    // 게시판 최대 신고 카운트
    private final Integer MAX_BOARD_REPORT_COUNT = 3;

    private final BoardReportRepository boardReportRepository;
    private final ReplyReportRepository replyReportRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    public void saveBoardReport(Board board, Account account, BoardReportForm reportForm) {
        BoardReport Boardreport = BoardReport.builder()
                .board(board)
                .account(account)
                .reportTime(LocalDateTime.now())
                .report_content(reportForm.getReport_content())
                .build();
        boardReportRepository.save(Boardreport);
        Integer boardReportCount = board.getReportCount();
        board.setReportCount(++boardReportCount);
        if (boardReportCount >= MAX_BOARD_REPORT_COUNT) {
            board.setIsReported(true);
        }
        boardRepository.save(board);
    }
    public void saveReplyReport(Reply reply, Account account, ReplyReportForm replyReportForm) {
        ReplyReport Replyreport = ReplyReport.builder()
                .account(account)
                .reply(reply)
                .reportTime(LocalDateTime.now())
                .report_content(replyReportForm.getReport_content())
                .build();
        replyReportRepository.save(Replyreport);
        reply.setReport(true);
        replyRepository.save(reply);
    }
}
