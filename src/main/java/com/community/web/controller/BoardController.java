package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountType;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.board.BoardSort;
import com.community.domain.bookmark.BookmarkRepository;
import com.community.infra.config.SecurityUser;
import com.community.web.dto.BoardForm;
import com.community.web.dto.ReplyForm;
import com.community.web.dto.BoardReportForm;
import com.community.domain.board.BoardRepository;
import com.community.domain.reply.ReplyRepository;
import com.community.service.BoardService;
import com.community.service.ReplyService;
import com.community.domain.likes.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
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

    private final ModelMapper modelMapper;

    @GetMapping("/board/{type}")
    public String boardTypeList(Model model,
                                @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                @PageableDefault(size = 5, page = 0, sort = "createDate",
                                        direction = Sort.Direction.ASC) Pageable pageable,
                                @PathVariable String type) {
        // Top5 게시물
        List<Board> top5Board = boardService.top5BoardLists();

        Page<Board> boardType = boardService.boardTypeControl(type, pageable);
        model.addAttribute("boardType", boardType);
        model.addAttribute("pageNo", page);
        model.addAttribute("top5Board", top5Board);
        model.addAttribute(type);

        return "board/boards";
    }

    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/board-new", method = RequestMethod.POST)
    public Long addNewBoard(@AuthenticationPrincipal SecurityUser securityUser,
                                @RequestParam Map<String, Object> params,
                                @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile) {

        BoardForm dto = modelMapper.map(params, BoardForm.class);
        Board savedBoard = boardService.saveNewBoard(multipartFile, dto, securityUser);

        return savedBoard.getId();
    }

    @GetMapping("/board/{type}/search")
    public String boardSearch(String keyword, Model model,
                              @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                              @PageableDefault(size = 5, page = 0, sort = "createDate",
                                      direction = Sort.Direction.ASC) Pageable pageable,
                              @PathVariable String type) {

        String searchType = BoardSort.valueOf(type.toUpperCase()).getValue();

        Page<Board> searchBoardResult = boardRepository.findByKeywordAndType(searchType, keyword, pageable);

        model.addAttribute("searchBoardResult", searchBoardResult);
        model.addAttribute("pageNo", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute(type);

        return "board/board-search";

    }

    // 위에서 요청한 리다이렉트 {boardId}로 다시 GetMapping
    @GetMapping("/board/detail/{boardId}")
    public String boardDetail(@PathVariable long boardId, @AuthenticationPrincipal SecurityUser securityUser,
                              HttpServletRequest request, HttpServletResponse response,
                              Model model) {

        Board currentBoard = boardService.findBoardById(boardId);

        if (securityUser != null) {
            Account account = securityUser.getAccount();
            model.addAttribute("account", account);
            model.addAttribute("likes", boardService.existLikeByBoard(currentBoard, account));
            model.addAttribute("bookmark", boardService.existBookmarkByBoard(currentBoard, account));
        }

        List<Board> top5Board = boardService.top5BoardLists();

        boardService.viewUpdate(boardId, request, response);

        model.addAttribute("board", currentBoard);
        model.addAttribute("top5Board", top5Board);

        return "board/board-detail";
    }

    @ResponseBody
    @RequestMapping(value = "/board/{id}/update", method = RequestMethod.PUT)
    public ResponseEntity boardUpdate(@PathVariable Long id,
                                      @AuthenticationPrincipal SecurityUser securityUser,
                                      @RequestParam Map<String, Object> params,
                                      @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile) {

        Board currentBoard = boardService.findBoardById(id);

        BoardForm dto = modelMapper.map(params, BoardForm.class);

        boardService.updateBoard(multipartFile, dto, securityUser, currentBoard);

        return ResponseEntity.ok().build();
    }

    // 게시물 삭제
    @GetMapping("/board/detail/{boardId}/delete")
    public String boardDelete(@PathVariable long boardId, @CurrentUser Account account, RedirectAttributes redirectAttributes) {
        log.info("보드 찾기 : " + boardId);
        Board currentBoard = boardRepository.findById(boardId);
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
