package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.Reply;
import com.community.domain.board.ReplyRepository;
import com.community.domain.inquire.Inquire;
import com.community.domain.inquire.InquireRepository;
import com.community.domain.report.BoardReport;
import com.community.domain.report.BoardReportRepository;
import com.community.domain.report.ReplyReportRepository;
import com.community.service.BoardService;
import com.community.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ManageController {

    private final BoardRepository boardRepository;
    private final BoardReportRepository boardReportRepository;
    private final ReplyRepository replyRepository;
    private final ReplyReportRepository replyReportRepository;
    private final InquireRepository inquireRepository;

    private final ReplyService replyService;
    private final BoardService boardService;

    @GetMapping("/manager/{type}")
    public String managerMainPage(@CurrentUser Account account, Model model,
                              @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                              @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                      direction = Sort.Direction.ASC) Pageable pageable,
                              @PathVariable String type) {
        switch (type) {
            case "board" :
                Page<Board> boardReportedLists = boardRepository.findByIsReportedOrderByCreateDateDesc(true, pageable);
                model.addAttribute("boardReportedLists", boardReportedLists);
                break;
            case "reply" :
                Page<Reply> replyReportedLists = replyRepository.findByIsReportedOrderByUploadTimeDesc(true, pageable);
                model.addAttribute("replyReportedLists", replyReportedLists);
                break;
            case "inquire" :
                Page<Inquire> inquireAnswerLists = inquireRepository.findByIsAnsweredOrderByUploadTimeDesc(false, pageable);
                model.addAttribute("inquireAnswerLists", inquireAnswerLists);
                break;
        }
        model.addAttribute("pageNo", page);
        model.addAttribute("replyService", replyService);
        model.addAttribute(type);
        model.addAttribute("account", account);

        return "manager/manager-main";

    }
    /*
    * 게시글 관련
    */
    @GetMapping("/manager/board/detail/{bid}")
    public String managerBoardDetailPage(@CurrentUser Account account, Model model, @PathVariable Long bid) {
        Optional<Board> currentBoard = boardRepository.findById(bid);

        List<BoardReport> boardReportList = boardReportRepository.findByBoardOrderByReportTimeDesc(currentBoard.get());


        model.addAttribute("board", currentBoard.get());
        model.addAttribute("reportList", boardReportList);
        model.addAttribute("replyService", replyService);
        model.addAttribute("account", account);

        return "manager/manager-board-detail";

    }

    @GetMapping("/manager/board/{bid}/reset")
    public String managerBoardReset(@CurrentUser Account account, Model model, @PathVariable Long bid,
                                    RedirectAttributes redirectAttributes) {
        Optional<Board> currentBoard = boardRepository.findById(bid);
        Board board = currentBoard.get();

        boardService.boardReportReset(board);

        redirectAttributes.addFlashAttribute("isUpdatedMessage", "게시물 신고가 초기화되었습니다.");
        return "redirect:/board/detail/{bid}";
    }

    @GetMapping("/manager/board/{bid}/delete")
    public String managerBoardDelete(@CurrentUser Account account, Model model, @PathVariable Long bid,
                                    RedirectAttributes redirectAttributes) {
        Optional<Board> currentBoard = boardRepository.findById(bid);
        Board board = currentBoard.get();

        boardRepository.delete(board);

        redirectAttributes.addFlashAttribute("deleteMessage", "게시물이 삭제되었습니다.");
        return "redirect:/board/free";
    }

    /*
     * 댓글 관련
     */
}
