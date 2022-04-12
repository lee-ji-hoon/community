package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import com.community.domain.board.Reply;
import com.community.web.dto.ReplyForm;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.ReplyRepository;
import com.community.domain.council.Council;
import com.community.domain.market.Market;
import com.community.domain.study.Meetings;
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
                .council(null)
                .uploadTime(LocalDateTime.now())
                .isReported(false)
                .reportCount(0)
                .build();
        replyRepository.save(reply);
        return reply;
    }

    public Reply saveMeetingsReply(@Valid ReplyForm replyForm, Account account, Meetings meetings) {
        Reply reply = Reply.builder()
                .content(replyForm.getContent())
                .account(account)
                .meetings(meetings)
                .council(null)
                .uploadTime(LocalDateTime.now())
                .isReported(false)
                .reportCount(0)
                .build();
        replyRepository.save(reply);

        return reply;
    }

    public Reply saveMarketReply(@Valid ReplyForm replyForm, Account account, Market market) {
        Reply reply = Reply.builder()
                .content(replyForm.getContent())
                .account(account)
                .market(market)
                .council(null)
                .uploadTime(LocalDateTime.now())
                .isReported(false)
                .reportCount(0)
                .build();
        replyRepository.save(reply);

        return reply;
    }

    public void saveCouncilReply(@Valid ReplyForm replyForm, Account account, Council council) {
        Reply reply = Reply.builder()
                .content(replyForm.getContent())
                .account(account)
                .board(null)
                .council(council)
                .uploadTime(LocalDateTime.now())
                .isReported(false)
                .reportCount(0)
                .build();
        replyRepository.save(reply);
    }

    public void updateReply(Long rid, String reply_content) {
        Reply reply = replyRepository.findByRid(rid);
        reply.setContent(reply_content);
        replyRepository.save(reply);
    }

    public int boardReplySize(Board board) {
        boolean isTrue = replyRepository.existsAllByBoard(board);
        if (isTrue) {
            List<Reply> replyList = replyRepository.findAllByBoardOrderByUploadTimeDesc(board);
            return replyList.size();
        }
        return 0;
    }

    public Boolean boardReplyPresent(Board board) {
        Boolean isTrue = null;
        List<Reply> replies = replyRepository.findAllByBoard(board);
        if (replies.isEmpty()) {
            isTrue = false;
            return isTrue;
        }
        isTrue = true;
        return isTrue;
    }

    public List<Reply> boardsReplyTop3List(Board board) {
        return replyRepository.findTop3ByBoardOrderByUploadTimeDesc(board);
    }
}
