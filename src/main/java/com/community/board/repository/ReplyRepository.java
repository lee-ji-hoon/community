package com.community.board.repository;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.council.Council;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByBoardOrderByUploadTimeDesc(Board board);
    List<Reply> findAllByCouncilOrderByUploadTimeDesc(Council council);

    boolean existsAllByBoard(Board board);

    Reply findByRid(Long rid);

    List<Reply> findAllByBoard(Board board);

    List<Reply> findAllByCouncil(Council council);

    List<Reply> findTop3ByBoardOrderByUploadTimeDesc(Board board);

}
