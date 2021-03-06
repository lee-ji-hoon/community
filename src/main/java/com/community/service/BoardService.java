package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.Board;
import com.community.domain.graduation.Graduation;
import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.infra.aws.S3Service;
import com.community.web.dto.BoardForm;
import com.community.domain.likes.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final S3Repository s3Repository;
    private final S3Service s3Service;

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public Board saveNewBoard(List<MultipartFile> multipartFile, Account account,
                              String post_sort, String post_sub_sort,
                              String post_title, String post_sub_title, String post_content) {

        Board board = Board.builder()
                .writer(account)
                .boardTitle(post_sort)
                .subBoardTitle(post_sub_sort)
                .title(post_title)
                .subTitle(post_sub_title)
                .content(post_content)
                .pageView(0)
                .uploadTime(LocalDateTime.now())
                .isReported(false)
                .reportCount(0)
                .build();

        uploadImage(multipartFile, board);

        return boardRepository.save(board);
    }

    public void deleteBoard(Board board) {
        List<S3> imageList = board.getImageList(); // ????????? ????????????

        for (S3 s3 : imageList) s3Service.deleteFile(s3.getImageName()); // ????????? ??????

        boardRepository.delete(board);
    }

    public void updateBoard(Board board, List<MultipartFile> multipartFile,
                                 String post_sort, String post_sub_sort,
                                 String post_title, String post_sub_title, String post_content) {

        board.setBoardTitle(post_sort);
        board.setSubBoardTitle(post_sub_sort);
        board.setTitle(post_title);
        board.setSubTitle(post_sub_title);
        board.setContent(post_content);
        board.setUpdateTime(LocalDateTime.now());

        uploadImage(multipartFile, board);
    }

    private void uploadImage(List<MultipartFile> multipartFile, Board board) {
        String uploadFolder = "board-img/";

        if (multipartFile != null) {
            List<String> imageFileList = s3Service.upload(multipartFile, uploadFolder);

            for (String imageFileName : imageFileList) {
                S3 s3 = new S3();
                s3.setImageName(uploadFolder + imageFileName);
                s3.setImagePath(S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + uploadFolder + imageFileName);
                s3.setBoard(board);

                S3 s3Image = s3Repository.save(s3);

                log.info("graduation Image :{}", board.getImageList());
                log.info("s3Image : {}", s3Image);
            }
        }
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
            msg = diffTime + "??? ???";
        } else if ((diffTime /= BoardService.SEC) < BoardService.MIN) {
            // min
            msg = diffTime + "??? ???";
        } else if ((diffTime /= BoardService.MIN) < BoardService.HOUR) {
            // hour
            msg = (diffTime) + "?????? ???";
        } else if ((diffTime /= BoardService.HOUR) < BoardService.DAY) {
            // day
            msg = (diffTime) + "??? ???";
        } else if ((diffTime /= BoardService.DAY) < BoardService.MONTH) {
            // day
            msg = (diffTime) + "??? ???";
        } else {
            msg = (diffTime) + "??? ???";
        }
        return msg;
    }

    /* ????????? ????????? ?????? ????????? */
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
                //???????????? ???????????? ????????????, ?????? ????????? ??? ????????? ??????????????? ????????? ????????? ??????
                if (cookie.getName().equals("|" + id + "|")) {
                    log.info("If Cookie Name : " + cookie.getName());
                    //?????? ????????? ????????? ??????
                    viewCookie = cookie;
                }
            }
        }else {
            log.info("Cookies Check Logic : None Cookie");
        }
        // ???????????? ????????? ????????? ??????
        if(viewCookie==null) {
            log.info("viewCookies Check Logic : None Cookie");
            try {
                // ???????????? ????????? ???????????? ????????? ??????
                pageViewUpdate(id);

                // ?????? ???????????? ??????????????? ?????? ??????
                Cookie newCookie=new Cookie("|"+id+"|","readCount");
                response.addCookie(newCookie);
            } catch (Exception e) {
                log.info("input cookie Error : " + e.getMessage());
                e.getStackTrace();
            }
            // ???????????? ????????? ????????? ???????????? ???????????? ??????
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

    public void boardReportReset(Board board) {
        board.setReportCount(0);
        board.setIsReported(false);

        boardRepository.save(board);
    }

    public List<Board> top5BoardLists() {
        List<Board> findView = boardRepository.findTop5ByIsReportedOrderByPageViewDesc(false);
        List<Board> findLike = boardRepository.findTop5ByIsReportedOrderByLikesListDesc(false);
        List<Board> findReply = boardRepository.findTop5ByIsReportedOrderByReplyListDesc(false);

        Map<Long, Board> listMap = new HashMap<>(5);
        for (Board board : findView) {
            listMap.put(board.getBid(), board);
        }
        for (Board board : findLike) {
            listMap.put(board.getBid(), board);
        }
        for (Board board : findReply) {
            listMap.put(board.getBid(), board);
        }

        List<Board> findTop5Boards = new ArrayList<>(listMap.values());
        Collections.sort(findTop5Boards, (b1, b2)
                -> (b2.getReplyList().size() + b2.getLikesList().size() + b2.getPageView())
                - (b1.getReplyList().size() + b1.getLikesList().size() + b1.getPageView()));
        while (findTop5Boards.size() > 5) {
            findTop5Boards.remove(findTop5Boards.size() - 1);
        }
        return findTop5Boards;
    }

}
