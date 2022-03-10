package com.community.report;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.report.form.BoardReportForm;
import com.community.report.form.ReplyReportForm;
import com.community.report.repository.BoardReportRepository;
import com.community.report.repository.ReplyReportRepository;
import com.community.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReportController {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    private final BoardReportRepository boardReportRepository;
    private final ReplyReportRepository replyReportRepository;

    private final ReportService reportService;

    private String updatePath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    @GetMapping("/board/report/{boardId}")
    public String boardDetailReport(@PathVariable Long boardId, @CurrentUser Account account, Model model, RedirectAttributes redirectAttributes) {
        Optional<Board> board = boardRepository.findById(boardId);
        String isReportedMessage = "";
        if (board.isEmpty()) {
            return "error-page";
        }
        Board currentBoard = boardRepository.findByBid(board.get().getBid());
        Boolean isReported = boardReportRepository.existsByAccountAndBoard(account, currentBoard);
        if (isReported) {
            redirectAttributes.addFlashAttribute("isReportedMessage","이미 신고 되었습니다.");
            return "redirect:/board/detail/{boardId}";
        }
        model.addAttribute("board", currentBoard);
        model.addAttribute(account);
        model.addAttribute(new BoardReportForm());
        return "report-form";
    }
    @GetMapping("/reply/report/{replyId}")
    public String replyDetailReport(@PathVariable Long replyId, @CurrentUser Account account, Model model, RedirectAttributes redirectAttributes) {
        Optional<Reply> reply = replyRepository.findById(replyId);
        String isReportedMessage = "";
        if (reply.isEmpty()) {
            return "error-page";
        }
        Reply currentReply = replyRepository.findByRid(reply.get().getRid());
        Board currentBoard = currentReply.getBoard();
        Boolean isReported = replyReportRepository.existsByAccountAndReply(account, currentReply);

        if (isReported) {
            redirectAttributes.addFlashAttribute("isReportedMessage","이미 신고 되었습니다.");
            String path = String.valueOf(currentReply.getBoard().getBid());
            return "redirect:/board/detail/" + updatePath(path);
        }
        model.addAttribute("reply", currentReply);
        model.addAttribute("r_board", currentBoard);
        model.addAttribute(account);
        model.addAttribute(new ReplyReportForm());
        return "report-form";
    }

    @PostMapping("/board/detail/{boardId}/report")
    public String boardReport(@PathVariable Long boardId, BoardReportForm boardReportForm, @CurrentUser Account account) {
        Board currentBoard = boardRepository.findByBid(boardId);
        reportService.saveBoardReport(currentBoard, account, boardReportForm);
        return "redirect:/board/detail/{boardId}";
    }
    @PostMapping("/reply/detail/{rid}/report")
    public String replyReport(@PathVariable Long rid, ReplyReportForm replyReportForm, @CurrentUser Account account) {
        Reply currentReply = replyRepository.findByRid(rid);
        log.info(currentReply.toString());
        reportService.saveReplyReport(currentReply, account, replyReportForm);
        String path = String.valueOf(currentReply.getBoard().getBid());
        return "redirect:/board/detail/" + updatePath(path);
    }

}
