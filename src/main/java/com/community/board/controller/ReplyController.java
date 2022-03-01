package com.community.board.controller;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.form.ReplyForm;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.board.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    private final ReplyService replyService;

    /* 댓글 작성 관련 로직 */
    @PostMapping("/board/detail/{boardId}/reply")
    public String boardReply(@PathVariable Long boardId, @Valid ReplyForm replyForm, @CurrentUser Account account) {
        Board currentBoard = boardRepository.findByBid(boardId);
        replyService.saveReply(replyForm, account, currentBoard);
        return "redirect:/board/detail/{boardId}";
    }

    @PostMapping("/board/detail/{boardId}/reply/update/{rid}")
    public String boardReplyUpdate(@PathVariable Long boardId,
                                   @PathVariable Long rid, @Valid ReplyForm replyForm) {
        replyService.updateReply(rid, replyForm);
        return "redirect:/board/detail/{boardId}";
    }

    @PostMapping("/board/detail/{boardId}/reply/delete/{rid}")
    public String boardReplyDelete(@PathVariable Long boardId,
                                   @PathVariable Long rid) {
        Reply findReply = replyRepository.findByRid(rid);
        replyRepository.delete(findReply);
        return "redirect:/board/detail/{boardId}";
    }

}
