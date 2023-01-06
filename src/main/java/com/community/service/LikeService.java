package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import com.community.domain.likes.LikeRepository;
import com.community.domain.likes.Likes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final CouncilRepository councilRepository;

    public Likes addLike(Account account, String postSort, Long postId) {
        Likes likes = Likes.builder()
                .account(account)
                .build();

        switch (postSort) {
            case "board":
                Board currentBoard = boardRepository.findById(postId).get();
                // 중복 좋아요 방지
                if(isNotAlreadyBoardLike(account, currentBoard)) {
                    likes.setBoard(currentBoard);
                    break;
                }
            case "council":
                Council currentCouncil = councilRepository.findByCid(postId);
                // 중복 좋아요 방지
                if(isNotAlreadyCouncilLike(account, currentCouncil)) {
                    likes.setCouncil(currentCouncil);
                    break;
                }
        }
        return likeRepository.save(likes);
    }

    public void deleteLike(Account account, String postSort, Long postId) {
        Likes likes;
        switch (postSort) {
            case "board":
                Board currentBoard = boardRepository.findById(postId).get();
                likes = likeRepository.findByBoardAndAccount(currentBoard, account);
                likeRepository.delete(likes);
                break;
            case "council":
                Council currentCouncil = councilRepository.findByCid(postId);
                likes = likeRepository.findByCouncilAndAccount(currentCouncil, account);
                likeRepository.delete(likes);
                break;
        }
    }

    private boolean isNotAlreadyBoardLike(Account account, Board board) {
        return likeRepository.findByAccountAndBoard(account, board).isEmpty();
    }

    private boolean isNotAlreadyCouncilLike(Account account, Council council) {
        return likeRepository.findByAccountAndCouncil(account, council).isEmpty();
    }

    public int boardLikeSize(Board board) {
        boolean isTrue = likeRepository.existsAllByBoard(board);
        if (isTrue) {
            List<Likes> likeList = likeRepository.findAllByBoard(board);
            return likeList.size();
        }
        return 0;
    }
}
