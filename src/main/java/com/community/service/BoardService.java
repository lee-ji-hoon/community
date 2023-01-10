package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.Board;
import com.community.domain.board.BoardSort;
import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.infra.aws.S3Service;
import com.community.infra.config.SecurityUser;
import com.community.web.dto.BoardForm;
import com.community.web.exception.IdNotFoundException;
import com.community.web.exception.IsReportedException;
import com.community.web.exception.NotOwnerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final S3Repository s3Repository;
    private final S3Service s3Service;
    private final LikeService likeService;
    private final BookmarkService bookmarkService;

    private final ModelMapper mapper;

    public Board saveNewBoard(List<MultipartFile> multipartFile, BoardForm dto, SecurityUser securityUser) {
        Board newBoard = setBasicInfo(mapper.map(dto, Board.class), securityUser.getAccount());
        uploadImage(multipartFile, newBoard);

        return boardRepository.save(newBoard);
    }

    public void deleteBoard(Board board) {
        List<S3> imageList = board.getImageList(); // 이미지 불러오기

        for (S3 s3 : imageList) s3Service.deleteFile(s3.getImageName()); // 이미지 삭제

        boardRepository.delete(board);
    }

    public boolean isBoardOwner(Account account, Board board) {

        if (!board.getWriter().equals(account)) {
            throw new NotOwnerException("잘못된 접근입니다.");
        }
        return true;
    }

    public void updateBoard(List<MultipartFile> multipartFile, BoardForm dto,
                            SecurityUser securityUser, Board board) {

        if (isBoardOwner(securityUser.getAccount(), board)) {

            mapper.map(dto, board);

            uploadImage(multipartFile, board);
        }

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

    public Page<Board> boardTypeControl(String type, Pageable pageable) {
        Page<Board> boardPage = boardRepository
                .findByBoardTitleAndIsReportedOrderByCreateDateDesc(
                        BoardSort.valueOf(type.toUpperCase()).getValue(), false, pageable
                );
        return boardPage;
    }

    /* 페이지 조회수 증가 서비스 */
    private void pageViewUpdate(Long boardId){
        Board board = boardRepository.findById(boardId).get();
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
            listMap.put(board.getId(), board);
        }
        for (Board board : findLike) {
            listMap.put(board.getId(), board);
        }
        for (Board board : findReply) {
            listMap.put(board.getId(), board);
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

    public Board findBoardById(Long boardId) {
        Optional<Board> optBoard = boardRepository.findById(boardId);
        optBoard.ifPresent( b -> {
            if (b.getIsReported()) {
                throw new IsReportedException(boardId + "신고된 게시글입니다.");
            }
        });
        return optBoard.orElseThrow( () -> new IdNotFoundException(boardId + "번 게시물은 존재하지 않습니다."));
    }

    public Board setBasicInfo(Board board, Account account) {
        board.setIsReported(false);
        board.setPageView(0);
        board.setReportCount(0);
        board.setWriter(account);
        return board;
    }

    public boolean existLikeByBoard(Board board, Account account) {
        return likeService.existLikeByBoardAndAccount(board, account);
    }

    public boolean existBookmarkByBoard(Board board, Account account) {
        return bookmarkService.existBookmarkByBoardAndAccount(board, account);
    }
}
