package com.community.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findAllById(long id);

    List<Board> findByBoardTitle(String boardTitle);

    List<Board> findAllByBoardTitle(String boardTitle);
}
