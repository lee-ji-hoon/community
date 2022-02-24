package com.community.report.repository;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.report.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {

    boolean existsByAccountAndBoard(Account account, Board board);
}
