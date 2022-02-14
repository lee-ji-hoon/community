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
                .uploadTime(LocalDateTime.now().minusHours(1))
                .writer(boardForm.getWriter())
                .build();
        return boardRepository.save(board);
    }

    public Board updateBoard(Long boardId, BoardForm boardForm) {
        Board board = boardRepository.findAllById(boardId);
        board.setTitle(boardForm.getTitle());
        board.setBoardTitle(boardForm.getBoardTitle());
        board.setPageView(0);
        board.setContent(boardForm.getContent());
        board.setWriter(boardForm.getWriter());
        board.setUpdateTime(LocalDateTime.now().minusHours(1));

        return boardRepository.save(board);
    }

    public void pageViewUpdate(Long boardId){
        int pageView = boardRepository.findByPageView(boardId);
        pageView += 1;
        Board board = boardRepository.findAllById(boardId);
        board.setPageView(pageView);
    }

}
