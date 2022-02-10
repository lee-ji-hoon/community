package com.community.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardRepository boardRepository;

    @GetMapping("")
    public String boardList(Model model) {
        List<Board> board = boardRepository.findAll();
        model.addAttribute("board", board);
        return "board/board-list";
    }

    @GetMapping("/write")
    public String boardForm(Model model) {
        model.addAttribute(new BoardForm());
        return "board/board-write";
    }

    @PostMapping("/detail")
    public String detailView(@PathVariable long boardId, Model model) {
        Board boardDetail = boardRepository.findById(boardId);
        model.addAttribute("board", boardDetail);
        return "/board/detail/{boardId}";
    }
}
