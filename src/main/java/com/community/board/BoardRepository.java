package com.community.board;

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

    boolean existsByBoardTitle(String title);

    List<Board> findAllByBoardTitle(String boardTitle);

    List<Board> findByTitle(String keyword);

    void deleteAllByWriterId(long writerId);
}
