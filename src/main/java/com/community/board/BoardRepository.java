package com.community.board;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findAllById(long id);

    List<Board> findAllByBoardTitle(String boardTitle);

    List<Board> findByTitle(String keyword);
}
