package com.community.board.controller;

import com.community.account.repository.AccountRepository;
import com.community.account.entity.Account;
import com.community.account.CurrentUser;
import com.community.board.entity.Board;
import com.community.board.form.BoardForm;
import com.community.board.form.ReplyForm;
import com.community.report.form.BoardReportForm;
import com.community.board.form.SearchForm;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.board.service.BoardService;
import com.community.board.service.ReplyService;
import com.community.like.LikeApiController;
import com.community.like.LikeRepository;
import com.community.like.LikeService;
import com.community.like.Likes;
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

    private final BoardService boardService;
    private final LikeService likeService;
    private final ReplyService replyService;
    private final LikeApiController likeApiController;

    //전체 게시물 조회
    @GetMapping("/board")
    public String boardList(Model model) {
        List<Board> boards = boardRepository.findAllByIsReportedOrderByUploadTimeDesc(false);
        model.addAttribute("board", boards);
        model.addAttribute("service", boardService);
        model.addAttribute("accountRepo", accountRepository);
        model.addAttribute("likeService", likeService);
        model.addAttribute("replyService", replyService);
        model.addAttribute("bt", "전체게시판");
        model.addAttribute(new SearchForm());
        return "board/board-list";
    }

    @GetMapping("/board/main")
    public String boardMain(Model model) {
        List<Board> boards = boardRepository.findAllByIsReportedOrderByUploadTimeDesc(true);
        model.addAttribute("board", boards);
        model.addAttribute("service", boardService);
        model.addAttribute("accountRepo", accountRepository);
        model.addAttribute("likeService", likeService);
        model.addAttribute("replyService", replyService);
        model.addAttribute(new SearchForm());
        return "board/board-main";
    }

    /* 게시물 작성 관련 */
    // 게시물 작성
    @GetMapping("/board/write")
    public String boardForm(@CurrentUser Account account, Model model) {

        model.addAttribute("account", account);
        model.addAttribute(new BoardForm());
        return "board/board-write";
    }

    // 게시물 작성 후 detail 페이지로 Post
    @PostMapping("/board/detail")
    public String detailView(@Valid BoardForm boardForm, Errors errors, RedirectAttributes redirectAttributes, @CurrentUser Account account) {
        if (errors.hasErrors()) {
            return "board/board-write";
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
        boardService.viewUpdate(boardId, request, response);
        Board detail = boardRepository.findByBid(boardId);
        Optional<Likes> likes = likeRepository.findByAccountAndBoard(account, detail);
        model.addAttribute("board", detail);
        model.addAttribute("account", account);
        model.addAttribute("service", boardService);
        model.addAttribute("accountRepo", accountRepository);
        model.addAttribute("likes", likes);
        model.addAttribute("likeService", likeService);
        model.addAttribute("reply", replyRepository.findAllByBoardOrderByUploadTimeDesc(detail));
        model.addAttribute("replyService", replyService);
        model.addAttribute(new ReplyForm());
        model.addAttribute(new BoardReportForm());
        return "board/detail";
    }

    /* 게시물 수정 및 관련 */
    // 게시글 수정
    @GetMapping("/board/{boardId}/edit")
    public String boardUpdateForm(@PathVariable long boardId, Model model) {
        Board board = boardRepository.findByBid(boardId);
        model.addAttribute("board", board);
        return "board/edit";
    }

    // 게시글 수정 후 {boardId}로 리다이렉트
    @PostMapping("/board/detail/{boardId}")
    public String boardUpdate(@PathVariable long boardId, BoardForm boardForm, Model model) {
        Board board = boardService.updateBoard(boardId, boardForm);
        model.addAttribute("board", board);
        return "redirect:/board/detail/{boardId}";
    }

    // 게시물 삭제
    @GetMapping("/board/{boardId}/delete")
    public String boardDelete(@PathVariable long boardId) {
        Board board = boardRepository.findByBid(boardId);

        boardRepository.delete(board);

        return "redirect:/board";
    }

    // TODO Summernote 사진 업로드 구현해야함.
    public void uploadFile() {

    }

    // 검색 기능
    @PostMapping("/board/search/{boardTitle}")
    public String searchPost(@PathVariable String boardTitle, SearchForm searchForm, Model model) {
        log.info("검색 조건 : " + searchForm.getSearchType());
        log.info("검색 키워드 : " + searchForm.getKeyword());
        log.info("검색 게시판 : " + searchForm.getBoardTitle());
        List<Board> searchPosts = boardService.searchPosts(searchForm.getSearchType(), searchForm.getKeyword(), searchForm.getBoardTitle());

        model.addAttribute("board", searchPosts);
        model.addAttribute("accountRepo", accountRepository);
        model.addAttribute("service", boardService);
        model.addAttribute("likeService", likeService);
        model.addAttribute("replyService", replyService);
        model.addAttribute("bt", boardTitle);

        model.addAttribute(new SearchForm());
        return "board/board-list";
    }

    @GetMapping("/board/search/{writerId}")
    public String findUserPost(@PathVariable long writerId, Model model) {
        List<Board> boards = boardRepository.findAllByWriterIdAndIsReportedOrderByUploadTime(writerId, false);
        Optional<Account> account = accountRepository.findById(writerId);
        model.addAttribute("board", boards);
        model.addAttribute("accountRepo", accountRepository);
        model.addAttribute("service", boardService);
        model.addAttribute("likeService", likeService);
        model.addAttribute("replyService", replyService);
        model.addAttribute("bt", account.get().getNickname());

        model.addAttribute(new SearchForm());
        return "board/board-list";
    }


    // 게시판 별로 분류
    @GetMapping("/board/bt/{boardTitle}")
    public String boardList(@PathVariable String boardTitle, Model model) {
        List<Board> boards = boardRepository.findAllByBoardTitleAndIsReportedOrderByUploadTimeDesc(boardTitle, false);
        model.addAttribute("board", boards);
        model.addAttribute("service", boardService);
        model.addAttribute("accountRepo", accountRepository);
        model.addAttribute("likeService", likeService);
        model.addAttribute("replyService", replyService);
        model.addAttribute("bt", boardTitle);

        model.addAttribute(new SearchForm());
        return "board/board-list";
    }

    // 좋아요 관련 내용
    @GetMapping("/board/detail/{boardId}/like")
    public String addLikeLink(@CurrentUser Account account, @PathVariable Long boardId) {
        likeApiController.addLike(account, boardId);
        return "redirect:/board/detail/{boardId}";
    }

    @GetMapping("/board/detail/{boardId}/like-cancel")
    public String cancelLikeLink(@CurrentUser Account account, @PathVariable Long boardId) {
        Board board = boardRepository.findByBid(boardId);
        boolean existLike = likeRepository.existsByAccountAndBoard(account, board);
        log.info("existLike = " + existLike);
        log.info(board.getBid().toString());
        log.info(account.getId().toString());
        if (existLike) {
            Likes likes = likeRepository.findByBoardAndAccount(board, account);
            log.info("likeId = " + likes);
            likeRepository.delete(likes);
            return "redirect:/board/detail/{boardId}";
        }
        return "redirect:/board/detail/{boardId}";
    }
}
