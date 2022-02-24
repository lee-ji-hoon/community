package com.community.report.repository;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.report.entity.BoardReport;
import com.community.report.entity.ReplyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyReportRepository extends JpaRepository<ReplyReport, Long> {

    boolean existsByAccountAndReply(Account account, Reply reply);

}
