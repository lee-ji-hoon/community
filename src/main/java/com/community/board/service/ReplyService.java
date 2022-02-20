package com.community.board.service;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.form.ReplyForm;
import com.community.board.repository.ReplyRepository;
import com.community.like.Likes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;

    public Reply saveReply(@Valid ReplyForm replyForm, Account account, Board board) {

        Reply reply = Reply.builder()
                .content(replyForm.getContent())
                .account(account)
                .board(board)
                .build();
        return replyRepository.save(reply);
    }

    public int boardReplySize(Board board) {
        boolean isTrue = replyRepository.existsAllByBoard(board);
        if (isTrue) {
            List<Reply> replyList = replyRepository.findAllByBoard(board);
            return replyList.size();
        }
        return 0;
    }
}