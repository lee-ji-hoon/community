package com.community.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board saveNewBoard(BoardForm boardForm) {
        Board board = Board.builder()
                .title(boardForm.getTitle())
                .content(boardForm.getContent())
                .uploadTime(LocalDateTime.now().minusHours(1))
                .writer(boardForm.getWriter())
                .build();
        boardRepository.save(board);
        return board;
    }

}
