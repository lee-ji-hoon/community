package com.community.board;

import com.community.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleContaining(String keyword);

    List<Board> findByWriterContaining(String keyword);

    List<Board> findByContentContaining(String keyword);

    List<Board> findByWriterId(long id);

    Board findAllByBid(long id);

    Board findByBid(long id);

    List<Board> findAllByBoardTitle(String boardTitle);

    List<Board> findTop4ByBoardTitleOrderByBidDesc(String boardTitle);

    void deleteAllByWriterId(long writerId);
}
