package com.community.board.repository;

import com.community.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleContainingOrderByUploadTimeDesc(String keyword);

    List<Board> findByWriterContainingOrderByUploadTimeDesc(String keyword);

    List<Board> findByContentContainingOrderByUploadTimeDesc(String keyword);

    List<Board> findAllByWriterIdOrderByUploadTimeDesc(long id);

    Board findAllByBid(long id);

    Board findByBid(long id);

    List<Board> findAllByBoardTitleOrderByUploadTimeDesc(String boardTitle);

    List<Board> findTop4ByBoardTitleOrderByBidDesc(String boardTitle);

    void deleteAllByWriterId(long writerId);
}
