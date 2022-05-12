package com.community.domain.report;

import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyReportRepository extends JpaRepository<ReplyReport, Long> {

    boolean existsByAccountAndReply(Account account, Reply reply);

    ReplyReport findByAccountAndReply(Account account, Reply reply);

}
