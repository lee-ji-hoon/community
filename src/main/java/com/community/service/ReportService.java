package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import com.community.domain.reply.Reply;
import com.community.domain.report.BoardReport;
import com.community.domain.report.ReplyReport;
import com.community.web.dto.BoardReportForm;
import com.community.domain.board.BoardRepository;
import com.community.domain.reply.ReplyRepository;
import com.community.web.dto.ReplyReportForm;
import com.community.domain.report.BoardReportRepository;
import com.community.domain.report.ReplyReportRepository;
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
    private final Integer MAX_BOARD_AND_REPLY_REPORT_COUNT = 3;

    private final BoardReportRepository boardReportRepository;
    private final ReplyReportRepository replyReportRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    public void saveBoardReport(Board board, Account account, BoardReportForm reportForm) {
        BoardReport boardReport = BoardReport.builder()
                .board(board)
                .account(account)
                .reportTime(LocalDateTime.now())
                .report_title(reportForm.getReport_title())
                .report_content(reportForm.getReport_content())
                .build();
        boardReportRepository.save(boardReport);
        board.increaseReportCount();
        if (board.getReportCount() >= MAX_BOARD_AND_REPLY_REPORT_COUNT) {
            board.setReported();
        }
        boardRepository.save(board);
    }
    public void saveReplyReport(Reply reply, Account account, ReplyReportForm replyReportForm) {
        ReplyReport Replyreport = ReplyReport.builder()
                .account(account)
                .reply(reply)
                .reportTime(LocalDateTime.now())
                .report_title(replyReportForm.getReport_title())
                .report_content(replyReportForm.getReport_content())
                .build();
        replyReportRepository.save(Replyreport);
        Integer replyReportCount = reply.getReportCount();
        reply.setReportCount(++replyReportCount);
        if (replyReportCount >= MAX_BOARD_AND_REPLY_REPORT_COUNT) {
            reply.setIsReported(true);
        }
        replyRepository.save(reply);
    }
}
