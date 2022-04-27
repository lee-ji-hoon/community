package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.board.Reply;
import com.community.domain.bookmark.Bookmark;
import com.community.domain.bookmark.BookmarkRepository;
import com.community.infra.aws.S3Repository;
import com.community.web.dto.BoardForm;
import com.community.web.dto.ReplyForm;
import com.community.web.dto.BoardReportForm;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.ReplyRepository;
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
    private final S3Repository s3Repository;

    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping("/board/{type}/search")
    public String boardSearch(String keyword, @CurrentUser Account account, Model model,
                              @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                              @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                      direction = Sort.Direction.ASC) Pageable pageable,
                              @PathVariable String type) {

        Page<Board> searchBoardResult = boardRepository.findByBoardTitleOrContentContainingOrTitleContainingAndIsReportedOrderByUploadTimeDesc(type, keyword, keyword, false, pageable);
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

    // 게시물 작성 후 detail 페이지로 Post
    /*@PostMapping("/board/detail")
    public String detailView(@Valid BoardForm boardForm, Errors errors, Model model,
                             RedirectAttributes redirectAttributes, @CurrentUser Account account) {
        boolean emailVerified = account.isEmailVerified();

        log.info("email 체크 : {}", emailVerified);
        if (!emailVerified) {
            model.addAttribute(account);
            redirectAttributes.addFlashAttribute("emailVerifiedChecked", "이메일 인증 후에 사용 가능합니다.");

            return "redirect:/board";
        }

        if (errors.hasErrors()) {
            return "board/boards";
        }

        Board savedBoard = boardService.saveNewBoard(boardForm, account);
        redirectAttributes.addAttribute("boardId", savedBoard.getBid());
        return "redirect:/board/detail/{boardId}";
    }*/

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

        // 최근에 올라온 게시물
        List<Board> recentlyBoards = boardRepository.findTop4ByIsReportedOrderByUploadTimeDesc(false);

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

    /*@PostMapping("/board/update/{boardId}")
    public String boardDetailUpdate(@PathVariable long boardId, @Valid BoardForm boardForm, Errors errors, Model model,
                                    RedirectAttributes redirectAttributes, @CurrentUser Account account) {
        boardService.updateBoard(boardId, boardForm);
        redirectAttributes.addFlashAttribute("isUpdatedMessage", "게시물이 수정되었습니다.");


        return "redirect:/board/detail/{boardId}";
    }*/
    // 게시글 수정 후 {boardId}로 리다이렉트
    /*@ResponseBody
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
    }*/

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
        if (account.getId().equals(currentBoard.getWriter().getId())) {
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
