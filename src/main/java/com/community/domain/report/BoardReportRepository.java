package com.community.domain.report;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {

    boolean existsByAccountAndBoard(Account account, Board board);
}
