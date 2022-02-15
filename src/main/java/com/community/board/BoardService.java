package com.community.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

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

    public String boardDateTime(LocalDateTime localDateTime){
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < BoardService.SEC) {
            // sec
            msg = diffTime + "초 전";
        } else if ((diffTime /= BoardService.SEC) < BoardService.MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= BoardService.MIN) < BoardService.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= BoardService.HOUR) < BoardService.DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= BoardService.DAY) < BoardService.MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }

    public Page<Board> sortBoard() {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Board> result = boardRepository.findAll(pageable);
        return result;
    }

    public List<Board> sortBoardTitle(String boardTitle) {
        List<Board> result = boardRepository.findAllByBoardTitle(boardTitle);
        return result;
    }


}
