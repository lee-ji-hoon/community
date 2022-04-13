package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.Board;
import com.community.web.dto.BoardForm;
import com.community.domain.likes.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.*;

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
                .subTitle(boardForm.getSubTitle())
                .content(boardForm.getContent())
                .boardTitle(boardForm.getBoardTitle())
                .subBoardTitle(boardForm.getSubBoardTitle())
                .pageView(0)
                .uploadTime(LocalDateTime.now())
                .isReported(false)
                .reportCount(0)
                .writer(account)
                .build();
        return boardRepository.save(board);
    }

    public Board updateBoard(Long boardId, BoardForm boardForm) {
        Board board = boardRepository.findByBid(boardId);
        board.setTitle(boardForm.getTitle());
        board.setSubBoardTitle(boardForm.getSubBoardTitle());
        board.setBoardTitle(boardForm.getBoardTitle());
        board.setSubTitle(boardForm.getSubTitle());
        board.setContent(boardForm.getContent());
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

    public Page<Board> boardPageSystem(String title, int page) {
        return boardRepository.findAllByBoardTitleOrderByUploadTimeDesc(title, PageRequest.of(page, 2, Sort.by(Sort.Direction.DESC, "idx")));
    }

    public Page<Board> boardPage(String boardTitle, int page) {
        return boardRepository.findByBoardTitleAndIsReportedOrderByUploadTimeDesc(boardTitle, false, PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "uploadTime")));
    }

    /* 페이지 조회수 증가 서비스 */
    private void pageViewUpdate(Long boardId){
        Board board = boardRepository.findByBid(boardId);
        Integer page = board.getPageView();
        board.setPageView(++page);
        boardRepository.save(board);
    }

    public List<Board> boardTitleList(String boardTitle) {
        return boardRepository.findAllByBoardTitleAndIsReportedOrderByUploadTimeDesc(boardTitle, false);
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

    public Boolean boardReportedOrNull(long bid) {
        Boolean errorBoard = null;
        Optional<Board> currentBoard = boardRepository.findById(bid);
        if (currentBoard.isEmpty() || currentBoard.get().getIsReported().equals(true)) {
            errorBoard = true;
            return errorBoard;
        }
        errorBoard = false;
        return errorBoard;
    }

    public void top5BoardLists() {
        List<Board> findView = boardRepository.findTop5ByIsReportedOrderByPageViewDesc(false);
        List<Board> findLike = boardRepository.findTop5ByIsReportedOrderByLikesListDesc(false);
        List<Board> findReply = boardRepository.findTop5ByIsReportedOrderByReplyListDesc(false);

        Map<Long, Board> listMap = new HashMap<>();

        for (Board board : findView) {
            listMap.put(board.getBid(), board);
            log.info("Top View Board={}", board.getBid());
        }
        System.out.println();
        for (Board board : findLike) {
            listMap.put(board.getBid(), board);
            log.info("Top Like Board={}", board.getBid());
        }
        System.out.println();
        for (Board board : findReply) {
            listMap.put(board.getBid(), board);
            log.info("Top Reply Board={}", board.getBid());
        }
        /*if (listMap.size() > 5) {
            for (int i = 6; i < listMap.size(); i++) {
            // index가 5이상인 애들 모두 제거
            }
        }*/
        for (Long bid : listMap.keySet()) {
            log.info("Top Posts={}", bid);
        }
    }

}
