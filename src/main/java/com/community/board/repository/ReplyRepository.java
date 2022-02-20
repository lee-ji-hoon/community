package com.community.board.repository;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByBoardOrderByUploadTimeDesc(Board board);

    boolean existsAllByBoard(Board board);

    Reply findByRid(Long rid);

}
