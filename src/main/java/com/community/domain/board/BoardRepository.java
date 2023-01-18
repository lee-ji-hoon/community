package com.community.domain.board;

import com.community.domain.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryExtension {

    // 매니저 기능 관련
    Page<Board> findByIsReportedOrderByCreateDateDesc(Boolean isReported, Pageable pageable);

    // 페이징 기능 관련
    Page<Board> findAll(Pageable pageable);


    long countAllByWriterAndBoardTitle(Account account, String boardTitle);

    Page<Board> findByWriterAndBoardTitleAndIsReported(Account account, String boardTitle, Boolean isReported, Pageable pageable);


    /* 검색 관련 쿼리 */
    List<Board> findByContentContainingOrderByCreateDateDesc(String keyword);
    List<Board> findByTitleContainingOrderByCreateDateDesc(String keyword);

    /* 신고된 게시글 제외 쿼리 */

    Page<Board> findByBoardTitleAndIsReportedOrderByCreateDateDesc(String boardTitle, Boolean isReported, Pageable pageable);


    /* 실시간 인기글 */
    List<Board> findTop5ByIsReportedOrderByPageViewDesc(Boolean isReported);
    List<Board> findTop5ByIsReportedOrderByLikesListDesc(Boolean isReported);
    List<Board> findTop5ByIsReportedOrderByReplyListDesc(Boolean isReported);

    Board findById(long id);

    List<Board> findByWriterId(long id);

    List<Board> findByWriter(Account account);

    @Override
    Optional<Board> findById(Long id);

    void deleteAllByWriterId(long writerId);
    /*
    *
    * 매니저 전용 쿼리
    *
    */
    /* 신고된 게시글 쿼리 */
    List<Board> findByIsReportedOrderByCreateDateDesc(Boolean isReported);

}
