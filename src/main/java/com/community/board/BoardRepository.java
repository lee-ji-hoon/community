package com.community.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findAllByBid(long id);

    Board findByBid(long id);

    List<Board> findAllByBoardTitle(String boardTitle);

    List<Board> findByTitle(String keyword);
}
