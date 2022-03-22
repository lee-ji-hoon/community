package com.community.like;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.repository.BoardRepository;
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

    public Likes addLike(Account account, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow();

        //중복 좋아요 방지
        if(isNotAlreadyLike(account, board)) {
            Likes save = likeRepository.save(new Likes(board, account));
            return save;
        }
        return null;
    }



    private boolean isNotAlreadyLike(Account account, Board board) {
        return likeRepository.findByAccountAndBoard(account, board).isEmpty();
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
