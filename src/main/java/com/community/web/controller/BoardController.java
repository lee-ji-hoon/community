package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountType;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.reply.Reply;
import com.community.domain.bookmark.Bookmark;
import com.community.domain.bookmark.BookmarkRepository;
import com.community.web.dto.BoardForm;
import com.community.web.dto.ReplyForm;
import com.community.web.dto.BoardReportForm;
import com.community.domain.board.BoardRepository;
import com.community.domain.reply.ReplyRepository;
import com.community.service.BoardService;
import com.community.service.ReplyService;
import com.community.domain.likes.LikeRepository;
import com.community.domain.likes.Likes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final BookmarkRepository bookmarkRepository;

    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping("/board/{type}/search")
    public String boardSearch(String keyword, @CurrentUser Account account, Model model,
                              @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                              @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                      direction = Sort.Direction.ASC) Pageable pageable,
                              @PathVariable String type) {
        String searchType = "";
        switch (type) {
            case "free" :
                searchType = "자유";
                break;
            case "forum" :
                searchType = "정보";
                break;
            case "qna" :
                searchType = "질문";
                break;
        }

        Page<Board> searchBoardResult = boardRepository.findByKeywordAndType(searchType, keyword, pageable);
        for (Board board : searchBoardResult) {
            log.info("boardTitle={}", board.getTitle());
        }
        model.addAttribute("searchBoardResult", searchBoardResult);
        model.addAttribute("pageNo", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("replyService", replyService);
        model.addAttribute(type);
        model.addAttribute(account);

        return "board/board-search";

    }

    @GetMapping("/board/{type}")
    public String boardTypeList(@CurrentUser Account account, Model model,
                                @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                        direction = Sort.Direction.ASC) Pageable pageable,
                                @PathVariable String type) {
        // Top5 게시물
        List<Board> top5Board = boardService.top5BoardLists();

        switch (type) {
            case "free" :
                Page<Board> boardFree = boardRepository.findByBoardTitleAndIsReportedOrderByUploadTimeDesc("자유", false, pageable);
                model.addAttribute("boardType", boardFree);
                break;
            case "forum" :
                Page<Board> boardForum= boardRepository.findByBoardTitleAndIsReportedOrderByUploadTimeDesc("정보", false, pageable);
                model.addAttribute("boardType", boardForum);
                break;
            case "qna" :
                Page<Board> boardQna = boardRepository.findByBoardTitleAndIsReportedOrderByUploadTimeDesc("질문", false, pageable);
                model.addAttribute("boardType", boardQna);
                break;
        }

        model.addAttribute("pageNo", page);
        model.addAttribute("top5Board", top5Board);
        model.addAttribute("service", boardService);
        model.addAttribute("replyService", replyService);
        model.addAttribute(new BoardForm());
        model.addAttribute(type);
        model.addAttribute(account);

        return "board/boards";
    }

    @ResponseBody
    @RequestMapping(value = "/board-new", method = RequestMethod.POST)
    public Long boardFormSubmit(@CurrentUser Account account,
                                     @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile,
                                     @RequestParam(value = "post_sort", required = false) String post_sort,
                                     @RequestParam(value = "post_sub_sort", required = false) String post_sub_sort,
                                     @RequestParam(value = "post_title", required = false ) String post_title,
                                     @RequestParam(value = "post_sub_title", required = false) String post_sub_title,
                                     @RequestParam(value = "post_content", required = false) String post_content) {
        Board newBoard = boardService.saveNewBoard(
                multipartFile, account,
                post_sort, post_sub_sort,
                post_title, post_sub_title, post_content);

        return newBoard.getBid();
    }

    /* 게시물 작성 관련 */

    // 위에서 요청한 리다이렉트 {boardId}로 다시 GetMapping
    @GetMapping("/board/detail/{boardId}")
    public String boardDetail(@PathVariable long boardId, @CurrentUser Account account,
                              HttpServletRequest request, HttpServletResponse response,
                              Model model) {

        List<Board> top5Board = boardService.top5BoardLists();
        model.addAttribute("account", account);
        Boolean hasBoardError = boardService.boardReportedOrNull(boardId);
        if (hasBoardError) {
            return "error-page";
        }

        boardService.viewUpdate(boardId, request, response);
        Board currentBoard = boardRepository.findByBid(boardId);

        // 좋아요 및 댓글
        Optional<Likes> likes = likeRepository.findByAccountAndBoard(account, currentBoard);
        Optional<Bookmark> existBookmark = bookmarkRepository.findByAccountAndBoard(account, currentBoard);
        List<Reply> replies = replyRepository.findAllByBoardOrderByUploadTimeDesc(currentBoard);

        model.addAttribute("board", currentBoard);
        model.addAttribute("service", boardService);
        model.addAttribute("likes", likes);
        model.addAttribute("bookmark", existBookmark);
        model.addAttribute("reply", replies);
        model.addAttribute("replyService", replyService);
        model.addAttribute("top5Board", top5Board);

        model.addAttribute(new ReplyForm());
        model.addAttribute(new BoardReportForm());
        model.addAttribute(new BoardForm(currentBoard));

        return "board/board-detail";
    }

    @ResponseBody
    @RequestMapping(value = "/board/{id}/update", method = RequestMethod.POST)
    public ResponseEntity boardUpdate(@PathVariable Long id,
                                           @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile,
                                           @RequestParam(value = "post_sort", required = false) String post_sort,
                                           @RequestParam(value = "post_sub_sort", required = false) String post_sub_sort,
                                           @RequestParam(value = "post_title", required = false ) String post_title,
                                           @RequestParam(value = "post_sub_title", required = false) String post_sub_title,
                                           @RequestParam(value = "post_content", required = false) String post_content) {

        Optional<Board> byId = boardRepository.findById(id);
        Board board = byId.get();

        boardService.updateBoard(board, multipartFile, post_sort,
                post_sub_sort, post_title, post_sub_title, post_content);
        return ResponseEntity.ok().build();
    }

    // 게시물 삭제
    @GetMapping("/board/detail/{boardId}/delete")
    public String boardDelete(@PathVariable long boardId, @CurrentUser Account account, RedirectAttributes redirectAttributes) {
        log.info("보드 찾기 : " + boardId);
        Board currentBoard = boardRepository.findByBid(boardId);
        String postSort = currentBoard.getBoardTitle();
        if (account.getId().equals(currentBoard.getWriter().getId()) || account.getAccountType().equals(AccountType.ROLE_ADMIN)) {
            log.info("보드 삭제 : " + currentBoard);
            boardService.deleteBoard(currentBoard);
            redirectAttributes.addFlashAttribute("deleteMessage", "해당 게시글이 삭제되었습니다.");
            switch (postSort) {
                case "자유" :
                    return "redirect:/board/free";
                case "정보" :
                    return "redirect:/board/forum";
                case "질문" :
                    return "redirect:/board/qna";
            }
        }
        redirectAttributes.addFlashAttribute("errorMessage", "게시물 삭제 권한이 없습니다.");
        return "redirect:/board/detail/{boardId}";
    }
}
