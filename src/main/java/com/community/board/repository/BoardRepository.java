package com.community.board.repository;

import com.community.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    /* 검색 관련 쿼리 */
    List<Board> findByTitleContainingOrderByUploadTimeDesc(String keyword);
    List<Board> findByTitleContainingAndWriterOrderByUploadTimeDesc(String keyword, String writer);
    List<Board> findByTitleContainingAndBoardTitleOrderByUploadTimeDesc(String keyword, String boardTitle);

    List<Board> findByWriterContainingOrderByUploadTimeDesc(String keyword);
    List<Board> findByWriterContainingAndWriterOrderByUploadTimeDesc(String keyword, String writer);
    List<Board> findByWriterContainingAndBoardTitleOrderByUploadTimeDesc(String keyword, String boardTitle);

    List<Board> findByContentContainingOrderByUploadTimeDesc(String keyword);
    List<Board> findByContentContainingAndWriterOrderByUploadTimeDesc(String keyword, String writer);
    List<Board> findByContentContainingAndBoardTitleOrderByUploadTimeDesc(String keyword, String boardTitle);


    /* 신고된 게시글 제외 쿼리 */
    List<Board> findAllByWriterIdAndIsReportedOrderByUploadTime(long id, Boolean isReported);
    List<Board> findAllByIsReportedOrderByUploadTimeDesc(Boolean isReported);
    List<Board> findTop4ByBoardTitleAndIsReportedOrderByUploadTimeDesc(String boardTitle, Boolean isReported);
    List<Board> findAllByBoardTitleAndIsReportedOrderByUploadTimeDesc(String boardTitle, Boolean isReported);

    List<Board> findTop4ByIsReportedOrderByUploadTimeDesc(Boolean isReported);

    List<Board> findTop5ByIsReportedOrderByPageViewDesc(Boolean isReported);


    Boolean findByBidAndIsReported(long bid, Boolean isReported);

    Board findByBid(long id);

    List<Board> findAllByWriterIdOrderByUploadTime(long id);

    void deleteAllByWriterId(long writerId);
}
