package com.community.board.controller;

import com.community.account.repository.AccountRepository;
import com.community.account.entity.Account;
import com.community.account.CurrentUser;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.form.BoardForm;
import com.community.board.form.ReplyForm;
import com.community.report.form.BoardReportForm;
import com.community.board.form.SearchForm;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.board.service.BoardService;
import com.community.board.service.ReplyService;
import com.community.like.LikeRepository;
import com.community.like.LikeService;
import com.community.like.Likes;
import com.community.report.repository.BoardReportRepository;
import com.community.report.repository.ReplyReportRepository;
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
        return "board/blogs";
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
        log.info(boardForm.getBoardTitle().toString());
        log.info(boardForm.getTitle().toString());
        log.info(boardForm.getWriter().toString());
        log.info(boardForm.getContent().toString());
        if (errors.hasErrors()) {
            return "board/blogs";
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
        List<Reply> replies = replyRepository.findAllByBoardOrderByUploadTimeDesc(detail);

        // 최근에 올라온 게시물
        List<Board> recentlyBoards = boardRepository.findTop4ByIsReportedOrderByUploadTimeDesc(false);

        // 게시물 작성자 account 불러오는 로직
        Board currentBoard = boardRepository.findByBid(boardId);
        Account boardOwner = accountRepository.findByNickname(currentBoard.getWriter());

        model.addAttribute("board", detail);
        model.addAttribute("account", account);
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
        return "board/blog-read";
    }

    /* 게시물 수정 및 관련 */
    // 게시글 수정
    @GetMapping("/board/{boardId}/edit")
    public String boardUpdateForm(@PathVariable long boardId, @CurrentUser Account account , Model model) {
        Board currentBoard = boardRepository.findByBid(boardId);
        if (account.getId().equals(currentBoard.getWriterId())) {
            Board board = boardRepository.findByBid(boardId);
            model.addAttribute("board", board);
            return "board/edit";
        }
        return "redirect:/board/detail/{boardId}";
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
    public String boardDelete(@PathVariable long boardId, @CurrentUser Account account) {
        Board currentBoard = boardRepository.findByBid(boardId);
        if (account.getId().equals(currentBoard.getWriterId())) {
            boardRepository.delete(currentBoard);
            return "redirect:/board";
        }
        return "redirect:/board/detail/{boardId}";
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
}
