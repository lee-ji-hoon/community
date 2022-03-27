package com.community.web.controller;

import com.community.domain.account.AccountRepository;
import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.board.Reply;
import com.community.web.dto.BoardForm;
import com.community.web.dto.ReplyForm;
import com.community.web.dto.BoardReportForm;
import com.community.web.dto.SearchForm;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.ReplyRepository;
import com.community.service.BoardService;
import com.community.service.ReplyService;
import com.community.domain.likes.LikeRepository;
import com.community.service.LikeService;
import com.community.domain.likes.Likes;
import com.community.domain.report.BoardReportRepository;
import com.community.domain.report.ReplyReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;
    private final ReplyRepository replyRepository;
    private final BoardReportRepository boardReportRepository;
    private final ReplyReportRepository replyReportRepository;

    private final BoardService boardService;
    private final LikeService likeService;
    private final ReplyService replyService;

    //전체 게시물 조회
    @GetMapping("/board")
    public String boardList(Model model, @CurrentUser Account account) {
        // 최근에 올라온 게시물
        List<Board> recentlyBoards = boardRepository.findTop4ByIsReportedOrderByUploadTimeDesc(false);

        // Top5 게시물
        List<Board> top5Board = boardRepository.findTop5ByIsReportedOrderByPageViewDesc(false);

        model.addAttribute("board", top5Board);
        model.addAttribute("recentlyBoards", recentlyBoards);
        model.addAttribute("service", boardService);
        model.addAttribute("accountRepo", accountRepository);
        model.addAttribute("account", account);
        model.addAttribute("likeService", likeService);
        model.addAttribute("replyService", replyService);
        model.addAttribute("bt", "전체게시판");
        model.addAttribute(new SearchForm());
        model.addAttribute(new BoardForm());
        return "board/boards";
    }

    /* 게시물 작성 관련 */

    // 게시물 작성 후 detail 페이지로 Post
    @PostMapping("/board/detail")
    public String detailView(@Valid BoardForm boardForm, Errors errors, RedirectAttributes redirectAttributes, @CurrentUser Account account) {
        if (errors.hasErrors()) {
            return "board/boards";
        }

        Board savedBoard = boardService.saveNewBoard(boardForm, account);
        redirectAttributes.addAttribute("boardId", savedBoard.getBid());
        return "redirect:/board/detail/{boardId}";
    }

    // 위에서 요청한 리다이렉트 {boardId}로 다시 GetMapping
    @GetMapping("/board/detail/{boardId}")
    public String boardDetail(@PathVariable long boardId, @CurrentUser Account account,
                              HttpServletRequest request, HttpServletResponse response,
                              Model model) {

        model.addAttribute("account", account);
        Boolean hasBoardError = boardService.boardReportedOrNull(boardId);
        if (hasBoardError) {
            return "error-page";
        }

        boardService.viewUpdate(boardId, request, response);
        Board detail = boardRepository.findByBid(boardId);

        // 최근에 올라온 게시물
        List<Board> recentlyBoards = boardRepository.findTop4ByIsReportedOrderByUploadTimeDesc(false);

        // 좋아요 및 댓글
        Optional<Likes> likes = likeRepository.findByAccountAndBoard(account, detail);
        List<Reply> replies = replyRepository.findAllByBoardOrderByUploadTimeDesc(detail);

        // 게시물 작성자 account 불러오는 로직
        Board currentBoard = boardRepository.findByBid(boardId);
        Account boardOwner = currentBoard.getWriter();

        model.addAttribute("board", detail);
        model.addAttribute("boardOwner", boardOwner);
        model.addAttribute("service", boardService);
        model.addAttribute("accountRepo", accountRepository);
        model.addAttribute("likes", likes);
        model.addAttribute("likeService", likeService);
        model.addAttribute("reply", replies);
        model.addAttribute("replyService", replyService);
        model.addAttribute("boardReport", boardReportRepository.existsByAccountAndBoard(account, detail));
        model.addAttribute("replyRepo", replyReportRepository);
        model.addAttribute("recentlyBoards", recentlyBoards);

        model.addAttribute(new ReplyForm());
        model.addAttribute(new BoardReportForm());
        model.addAttribute(new BoardForm());

        return "board/board-detail";
    }

    // 게시글 수정 후 {boardId}로 리다이렉트
    @ResponseBody
    @RequestMapping(value = "/board/detail/update")
    public String boardUpdate(BoardForm boardForm, @CurrentUser Account account,
                              @RequestParam(value = "bid") String bid,
                              @RequestParam(value = "boardTitle") String boardTitle,
                              @RequestParam(value = "subBoardTitle") String subBoardTitle,
                              @RequestParam(value = "title") String title,
                              @RequestParam(value = "subTitle") String subTitle,
                              @RequestParam(value = "content") String content) {
        Long boardId = Long.valueOf(bid);
        Board board = boardRepository.findByBid(boardId);
        String message = null;
        if (account.getId().equals(board.getWriter().getId())) {
            boardForm.setBoardTitle(boardTitle);
            boardForm.setSubBoardTitle(subBoardTitle);
            boardForm.setTitle(title);
            boardForm.setSubTitle(subTitle);
            boardForm.setContent(content);
            boardService.updateBoard(boardId, boardForm);
            message = "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                    "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                    "        <i class=\"icon-feather-x\"></i>\n" +
                    "    </button>\n" +
                    "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                    "    <p class=\"text-white text-opacity-75\">게시물이 수정되었습니다.</p>\n" +
                    "</div>";
            return message;
        }
        log.info("잘못된 게시물 수정 요청 : bid = " + boardId + " accountId = " + account.getId());
        message = "잘못된 요청입니다.";
        return message;
    }

    // 게시물 삭제
    @GetMapping("/board/detail/{boardId}/delete")
    public String boardDelete(@PathVariable long boardId, @CurrentUser Account account, RedirectAttributes redirectAttributes) {
        log.info("보드 찾기 : " + boardId);
        Board currentBoard = boardRepository.findByBid(boardId);
        if (account.getId().equals(currentBoard.getWriter().getId())) {
            log.info("보드 삭제 : " + currentBoard);
            boardRepository.deleteById(currentBoard.getBid());
            redirectAttributes.addFlashAttribute("deleteMessage", "해당 게시글이 삭제되었습니다.");
            return "redirect:/board";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "게시물 삭제 권한이 없습니다.");
        return "redirect:/board/detail/{boardId}";
    }

    // TODO Summernote 사진 업로드 구현해야함.
    public void uploadFile() {

    }
}
