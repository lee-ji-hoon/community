package com.community.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public Board saveNewBoard(@Valid BoardForm boardForm) {
        Board board = Board.builder()
                .title(boardForm.getTitle())
                .content(boardForm.getContent())
                .boardTitle(boardForm.getBoardTitle())
                .pageView(0)
                .uploadTime(LocalDateTime.now())
                .writer(boardForm.getWriter())
                .build();
        return boardRepository.save(board);
    }

    public Board updateBoard(Long boardId, BoardForm boardForm) {
        Board board = boardRepository.findAllById(boardId);
        board.setTitle(boardForm.getTitle());
        board.setBoardTitle(boardForm.getBoardTitle());
        board.setContent(boardForm.getContent());
        board.setWriter(boardForm.getWriter());
        board.setUpdateTime(LocalDateTime.now());

        return boardRepository.save(board);
    }

    public void pageViewUpdate(Long boardId){
        Board board = boardRepository.findAllById(boardId);
        Integer page = board.getPageView();
        board.setPageView(++page);
        boardRepository.save(board);
    }

}
