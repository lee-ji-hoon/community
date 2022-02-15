package com.community.like;

import com.community.account.Account;
import com.community.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByAccountAndBoard(Account account, Board board);
}
