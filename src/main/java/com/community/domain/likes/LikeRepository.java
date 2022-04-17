package com.community.domain.likes;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import com.community.domain.council.Council;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByAccountAndBoard(Account account, Board board);

    Optional<Likes> findByAccountAndCouncil(Account account, Council council);

    List<Likes> findAllByBoard(Board board);

    List<Likes> findAllByCouncil(Council council);

    Likes findByBoardAndAccount(Board board, Account account);

    Likes findByCouncilAndAccount(Council council, Account account);



    boolean existsAllByBoard(Board board);

    boolean existsByAccountAndBoard(Account account, Board board);

//    List<Likes> findTop4ByBoardOrderByBoard(List<Board> board);

}
