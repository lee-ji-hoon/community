package com.community.domain.likes;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByAccountAndBoard(Account account, Board board);

    List<Likes> findAllByBoard(Board board);

    Likes findByBoardAndAccount(Board board, Account account);

    boolean existsAllByBoard(Board board);

    boolean existsByAccountAndBoard(Account account, Board board);

//    List<Likes> findTop4ByBoardOrderByBoard(List<Board> board);

}
