package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.board.Board;
import com.community.domain.board.Reply;
import com.community.domain.council.CouncilRepository;
import com.community.domain.market.MarketRepository;
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
    private final CouncilRepository councilRepository;
    private final MarketRepository marketRepository;

    public Reply addReply(Account account, String postSort, Long postId, String content) {
        Reply reply = Reply.builder()
                .account(account)
                .content(content)
                .isReported(false)
                .reportCount(0)
                .uploadTime(LocalDateTime.now())
                .build();

        switch (postSort) {
            case "market":
                Market currentMarket = marketRepository.findByMarketId(postId);
                reply.setMarket(currentMarket);
                break;
            case "board":
                Board currentBoard = boardRepository.findById(postId).get();
                reply.setBoard(currentBoard);
                break;
            case "council":
                Council currentCouncil = councilRepository.findByCid(postId);
                reply.setCouncil(currentCouncil);
                break;
        }
        return replyRepository.save(reply);
    }

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

    public int councilReplySize(Council council) {
        boolean isTrue = replyRepository.existsAllByCouncil(council);
        if (isTrue) {
            List<Reply> replyList = replyRepository.findAllByCouncilOrderByUploadTimeDesc(council);
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
