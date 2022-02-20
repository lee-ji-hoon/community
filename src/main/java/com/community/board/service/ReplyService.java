package com.community.board.service;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.form.BoardForm;
import com.community.board.form.ReplyForm;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.like.Likes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public Reply saveReply(@Valid ReplyForm replyForm, Account account, Board board) {

        Reply reply = Reply.builder()
                .content(replyForm.getContent())
                .account(account)
                .board(board)
                .uploadTime(LocalDateTime.now())
                .build();
        return replyRepository.save(reply);
    }

    public void updateReply(Long rid, ReplyForm replyForm) {
        Reply reply = replyRepository.findByRid(rid);
        reply.setContent(replyForm.getContent());
        reply.setUploadTime(LocalDateTime.now());
        replyRepository.save(reply);
    }

    /*public Board updateBoard(Long boardId, BoardForm boardForm) {
        Board board = boardRepository.findAllByBid(boardId);
        board.setTitle(boardForm.getTitle());
        board.setBoardTitle(boardForm.getBoardTitle());
        board.setContent(boardForm.getContent());
        board.setWriter(boardForm.getWriter());
        board.setUpdateTime(LocalDateTime.now());

        return boardRepository.save(board);
    }*/

    public int boardReplySize(Board board) {
        boolean isTrue = replyRepository.existsAllByBoard(board);
        if (isTrue) {
            List<Reply> replyList = replyRepository.findAllByBoardOrderByUploadTimeDesc(board);
            return replyList.size();
        }
        return 0;
    }
}
