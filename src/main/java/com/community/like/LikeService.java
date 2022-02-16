package com.community.like;

import com.community.account.Account;
import com.community.board.Board;
import com.community.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;

    public boolean addLike(Account account, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow();

        //중복 좋아요 방지
        if(isNotAlreadyLike(account, board)) {
            likeRepository.save(new Likes(board, account));
            return true;
        }
        return false;
    }
    private boolean isNotAlreadyLike(Account account, Board board) {
        return likeRepository.findByAccountAndBoard(account, board).isEmpty();
    }
}
