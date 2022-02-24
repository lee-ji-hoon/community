package com.community.board.service;

import com.community.account.entity.Account;
import com.community.board.repository.BoardRepository;
import com.community.board.entity.Board;
import com.community.board.form.BoardForm;
import com.community.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public Board saveNewBoard(@Valid BoardForm boardForm, Account account) {

        Board board = Board.builder()
                .title(boardForm.getTitle())
                .content(boardForm.getContent())
                .boardTitle(boardForm.getBoardTitle())
                .pageView(0)
                .uploadTime(LocalDateTime.now())
                .isReported(false)
                .writer(boardForm.getWriter())
                .writerId(account.getId())
                .build();
        return boardRepository.save(board);
    }

    public Board updateBoard(Long boardId, BoardForm boardForm) {
        Board board = boardRepository.findByBid(boardId);
        if (board.getIsReported()) {
            return boardRepository.save(board);
        }
        board.setTitle(boardForm.getTitle());
        board.setBoardTitle(boardForm.getBoardTitle());
        board.setContent(boardForm.getContent());
        board.setWriter(boardForm.getWriter());
        board.setUpdateTime(LocalDateTime.now());
        return boardRepository.save(board);

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
        Sort sort = Sort.by("uploadTime").descending();
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Board> result = boardRepository.findAll(pageable);
        return result;
    }

    public List<Board> mainBoardList(String boardTitle) {
        return boardRepository.findTop4ByBoardTitleAndIsReportedOrderByUploadTimeDesc(boardTitle, false);
    }

    public List<Board> searchPosts(String searchType, String keyword, String boardTitle) {
        //TODO boardTitle 검색 구현하기
        if (boardTitle.equals("전체게시판")) {
            switch (searchType) {
                case "title":
                    log.info("검색 타입 = " + searchType);
                    log.info("키워드 = " + keyword);
                    log.info(String.valueOf(boardRepository.findByTitleContainingOrderByUploadTimeDesc(keyword)));
                    return boardRepository.findByTitleContainingOrderByUploadTimeDesc(keyword);
                case "content":
                    log.info("검색 타입 = " + searchType);
                    log.info("키워드 = " + keyword);
                    log.info(String.valueOf(boardRepository.findByContentContainingOrderByUploadTimeDesc(keyword)));
                    return boardRepository.findByContentContainingOrderByUploadTimeDesc(keyword);
                case "writer":
                    log.info("검색 타입 = " + searchType);
                    log.info("키워드 = " + keyword);
                    log.info(String.valueOf(boardRepository.findByWriterContainingOrderByUploadTimeDesc(keyword)));
                    return boardRepository.findByWriterContainingOrderByUploadTimeDesc(keyword);
            }
        } else if (boardTitle.equals("자유게시판") || boardTitle.equals("정보공유게시판") || boardTitle.equals("신입생게시판") || boardTitle.equals("졸업생게시판")) {
            switch (searchType) {
                case "title":
                    log.info("검색 타입 = " + searchType);
                    log.info("키워드 = " + keyword);
                    log.info("게시판 = " + boardTitle);
                    log.info(String.valueOf(boardRepository.findByTitleContainingAndBoardTitleOrderByUploadTimeDesc(keyword, boardTitle)));
                    return boardRepository.findByTitleContainingAndBoardTitleOrderByUploadTimeDesc(keyword, boardTitle);
                case "content":
                    log.info("검색 타입 = " + searchType);
                    log.info("키워드 = " + keyword);
                    log.info("게시판 = " + boardTitle);
                    log.info(String.valueOf(boardRepository.findByContentContainingAndBoardTitleOrderByUploadTimeDesc(keyword, boardTitle)));
                    return boardRepository.findByContentContainingAndBoardTitleOrderByUploadTimeDesc(keyword, boardTitle);
                case "writer":
                    log.info("검색 타입 = " + searchType);
                    log.info("키워드 = " + keyword);
                    log.info("게시판 = " + boardTitle);
                    log.info(String.valueOf(boardRepository.findByWriterContainingAndBoardTitleOrderByUploadTimeDesc(keyword, boardTitle)));
                    return boardRepository.findByWriterContainingAndBoardTitleOrderByUploadTimeDesc(keyword, boardTitle);
            }
        } else {
            switch (searchType) {
                case "title":
                    log.info("검색 타입 = " + searchType);
                    log.info("키워드 = " + keyword);
                    log.info("게시판 = " + boardTitle);
                    log.info(String.valueOf(boardRepository.findByTitleContainingAndWriterOrderByUploadTimeDesc(keyword, boardTitle)));
                    return boardRepository.findByTitleContainingAndWriterOrderByUploadTimeDesc(keyword, boardTitle);
                case "content":
                    log.info("검색 타입 = " + searchType);
                    log.info("키워드 = " + keyword);
                    log.info("게시판 = " + boardTitle);
                    log.info(String.valueOf(boardRepository.findByWriterContainingAndWriterOrderByUploadTimeDesc(keyword, boardTitle)));
                    return boardRepository.findByWriterContainingAndWriterOrderByUploadTimeDesc(keyword, boardTitle);
                case "writer":
                    log.info("검색 타입 = " + searchType);
                    log.info("키워드 = " + keyword);
                    log.info("게시판 = " + boardTitle);
                    log.info(String.valueOf(boardRepository.findByContentContainingAndWriterOrderByUploadTimeDesc(keyword, boardTitle)));
                    return boardRepository.findByContentContainingAndWriterOrderByUploadTimeDesc(keyword, boardTitle);
            }
        }

        log.info("검색 조건 및 키워드가 존재하지 않음으로 전체 검색");
        return boardRepository.findAll();
    }

    /* 페이지 조회수 증가 서비스 */
    private void pageViewUpdate(Long boardId){
        Board board = boardRepository.findByBid(boardId);
        Integer page = board.getPageView();
        board.setPageView(++page);
        boardRepository.save(board);
    }

    public void viewUpdate(long id, HttpServletRequest request, HttpServletResponse response) {
        Cookie viewCookie=null;
        Cookie[] cookies=request.getCookies();
        log.info("cookie : " + Arrays.toString(cookies));

        if(cookies !=null) {
            for (Cookie cookie : cookies) {
                log.info(cookie.getName());
                //만들어진 쿠키들을 확인하며, 만약 들어온 적 있다면 생성되었을 쿠키가 있는지 확인
                if (cookie.getName().equals("|" + id + "|")) {
                    log.info("If Cookie Name : " + cookie.getName());
                    //찾은 쿠키를 변수에 저장
                    viewCookie = cookie;
                }
            }
        }else {
            log.info("Cookies Check Logic : None Cookie");
        }
        // 만들어진 쿠키가 없음을 확인
        if(viewCookie==null) {
            log.info("viewCookies Check Logic : None Cookie");
            try {
                // 만들어진 쿠키가 없으므로 조회수 증가
                pageViewUpdate(id);

                // 해당 페이지를 다녀갔다는 쿠키 생성
                Cookie newCookie=new Cookie("|"+id+"|","readCount");
                response.addCookie(newCookie);
            } catch (Exception e) {
                log.info("input cookie Error : " + e.getMessage());
                e.getStackTrace();
            }
            // 만들어진 쿠키가 있으면 증가로직 진행하지 않음
        }else {
            String value=viewCookie.getValue();
            log.info("viewCookie Check Logic : exist Cookie Value = " + value);
        }
    }

}
