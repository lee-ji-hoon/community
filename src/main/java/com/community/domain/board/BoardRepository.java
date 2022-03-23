package com.community.domain.board;

import com.community.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 페이징 기능 관련

    Page<Board> findAll(Pageable pageable);

    Page<Board> findAllByBoardTitleOrderByUploadTimeDesc(String title, Pageable pageable);

    /* 검색 관련 쿼리 */
    List<Board> findByContentContainingOrderByUploadTimeDesc(String keyword);
    List<Board> findByTitleContainingOrderByUploadTimeDesc(String keyword);

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