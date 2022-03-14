package com.community.report;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.council.Council;
import com.community.council.CouncilRepository;
import com.community.market.Market;
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
    private final CouncilRepository councilRepository;

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
            redirectAttributes.addFlashAttribute("isReportedMessage","이미 신고한 게시물입니다.");
            return "redirect:/board/detail/{boardId}";
        }
        model.addAttribute("board", currentBoard);
        model.addAttribute("boardOptional", board);
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
        Optional<Board> currentBoard = Optional.ofNullable(currentReply.getBoard());
        Optional<Council> currentCouncil = Optional.ofNullable(currentReply.getCouncil());
        Optional<Market> currentMarket = Optional.ofNullable(currentReply.getMarket());
        if (currentBoard.isPresent()) {
            Boolean isReported = replyReportRepository.existsByAccountAndReply(account, currentReply);
            if (isReported) {
                redirectAttributes.addFlashAttribute("isReportedReplyMessage","이미 신고한 댓글입니다.");
                String path = String.valueOf(currentReply.getBoard().getBid());
                return "redirect:/board/detail/" + updatePath(path);
            }
        }
        if (currentCouncil.isPresent()) {
            Boolean isReported = replyReportRepository.existsByAccountAndReply(account, currentReply);
            if (isReported) {
                redirectAttributes.addFlashAttribute("isReportedReplyMessage","이미 신고한 댓글입니다.");
                String path = String.valueOf(currentReply.getCouncil().getCid());
                return "redirect:/council/detail/" + updatePath(path);
            }
        }
        if (currentMarket.isPresent()) {
            Boolean isReported = replyReportRepository.existsByAccountAndReply(account, currentReply);
            if (isReported) {
                redirectAttributes.addFlashAttribute("isReportedReplyMessage","이미 신고한 댓글입니다.");
                String path = String.valueOf(currentReply.getMarket().getMarketId());
                return "redirect:/market/" + updatePath(path);
            }
        }




        model.addAttribute("reply", currentReply);
        model.addAttribute("r_board", currentBoard);
        model.addAttribute("r_council", currentCouncil);
        model.addAttribute("r_market", currentMarket);
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
    public String replyReport(@PathVariable Long rid, ReplyReportForm replyReportForm, @CurrentUser Account account, RedirectAttributes redirectAttributes) {
        Reply currentReply = replyRepository.findByRid(rid);
        if (currentReply.getBoard()!=null) {
            reportService.saveReplyReport(currentReply, account, replyReportForm);
            String path = String.valueOf(currentReply.getBoard().getBid());
            redirectAttributes.addFlashAttribute("reportCompleteMessage","신고 접수되었습니다.");
            return "redirect:/board/detail/" + updatePath(path);
        }
        if (currentReply.getCouncil()!=null) {
            reportService.saveReplyReport(currentReply, account, replyReportForm);
            String path = String.valueOf(currentReply.getCouncil().getCid());
            redirectAttributes.addFlashAttribute("reportCompleteMessage","신고 접수되었습니다.");
            return "redirect:/council/detail/" + updatePath(path);
        }
        if (currentReply.getMarket() != null) {
            reportService.saveReplyReport(currentReply, account, replyReportForm);
            String path = String.valueOf(currentReply.getMarket().getMarketId());
            redirectAttributes.addFlashAttribute("reportCompleteMessage","신고 접수되었습니다.");
            return "redirect:/market/" + updatePath(path);
        }
        return "redirect:/error";
    }

}
