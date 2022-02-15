package com.community.board;

import com.community.account.Account;
import com.community.account.CurrentUser;
import com.community.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    //전체 게시물 조회
    @GetMapping("/board")
    public String boardList(Model model) {
        List<Board> findAllBoard = boardRepository.findAll();
        model.addAttribute("boards", findAllBoard);
        model.addAttribute("service", boardService);
        return "board/board-list";
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
    public String detailView(@Valid BoardForm boardForm, Errors errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "board/board-write";
        }
        Board savedBoard = boardService.saveNewBoard(boardForm);
        redirectAttributes.addAttribute("boardId", savedBoard.getId());
        return "redirect:/board/detail/{boardId}";
    }
    // 위에서 요청한 리다이렉트 {boardId}로 다시 GetMapping
    @GetMapping("/board/detail/{boardId}")
    public String boardDetail(@PathVariable long boardId,@CurrentUser Account account, Model model) {
        boardService.pageViewUpdate(boardId);
        Board detail = boardRepository.findAllById(boardId);
        model.addAttribute("board", detail);
        model.addAttribute("account", account);
        return "board/detail";
    }

    /* 게시물 수정 및 관련 */
    // 게시글 수정
    @GetMapping("/board/{boardId}/edit")
    public String boardUpdateForm(@PathVariable long boardId, Model model) {
        Board board = boardRepository.findAllById(boardId);
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
    public String boardDelete(@PathVariable long boardId){
        Board board = boardRepository.findAllById(boardId);
        boardRepository.delete(board);
        return "redirect:/board";
    }

    // TODO Summernote 사진 업로드 구현해야함.
    public void uploadFile() {

    }

    @GetMapping("/board/bt/{boardTitle}")
    public String boardList(@PathVariable String boardTitle, Model model) {
        List<Board> boards = boardRepository.findAllByBoardTitle(boardTitle);
        model.addAttribute("board", boards);
        model.addAttribute("service", boardService);
        return "board/board-title";
    }

}
