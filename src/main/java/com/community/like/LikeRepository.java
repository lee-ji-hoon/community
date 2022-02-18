package com.community.like;

import com.community.account.entity.Account;
import com.community.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByAccountAndBoard(Account account, Board board);

    List<Likes> findAllByBoard(Board board);

    Likes findByBoard(Board board);

    void deleteByAccountAndBoard(Account account, Board board);

    boolean existsAllByBoard(Board board);

    boolean existsByBoard(Board board);
    boolean existsByAccountAndBoard(Account account, Board board);

}
