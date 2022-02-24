package com.community.report;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.report.form.BoardReportForm;
import com.community.report.form.ReplyReportForm;
import com.community.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReportController {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    private final ReportService reportService;

    @PostMapping("/board/detail/{boardId}/report")
    public String boardReport(@PathVariable Long boardId, BoardReportForm boardReportForm, @CurrentUser Account account) {
        Board currentBoard = boardRepository.findByBid(boardId);
        reportService.saveBoardReport(currentBoard, account, boardReportForm);
        return "redirect:/board/detail/{boardId}";
    }
    @PostMapping("/board/detail/{boardId}/reply/{rid}/report")
    public String replyReport(@PathVariable Long boardId, @PathVariable Long rid, ReplyReportForm replyReportForm, @CurrentUser Account account) {
        Reply currentReply = replyRepository.findByRid(rid);
        log.info(currentReply.toString());
        reportService.saveReplyReport(currentReply, account, replyReportForm);
        return "redirect:/board/detail/{boardId}";
    }

}
