package com.community.board.controller;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.account.repository.AccountRepository;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.form.ReplyForm;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.board.service.ReplyService;
import com.community.like.Likes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;
    private final ReplyRepository replyRepository;

    private final ReplyService replyService;

    // 좋아요 관련 내용
    @ResponseBody
    @RequestMapping(value = "/board/detail/reply")
    public int addLikeLink(@RequestParam(value = "r_board_id", required=false) Long r_board_id,
                           @RequestParam(value = "r_account_id", required=false) Long r_account_id,
                           @RequestParam(value = "r_content", required=false) String r_content,
                           ReplyForm replyForm){
        log.info("댓글 작성 호출");
        log.info(r_board_id + "r_board_id");
        log.info(r_account_id + "r_account_id");
        log.info(r_content + "r_content");

        replyForm.setContent(r_content);
        Board currentBoard = boardRepository.findByBid(r_board_id);
        Optional<Account> currentAccount = accountRepository.findById(r_account_id);
        if (currentAccount.isPresent()) {
            String accountEmail = currentAccount.get().getEmail();
            Account account = accountRepository.findByEmail(accountEmail);
            replyService.saveReply(replyForm, account, currentBoard);
            List<Reply> replies = replyRepository.findAll();
            int reply_size = replies.size();
            return reply_size;
        }
        List<Reply> replies = replyRepository.findAll();
        int reply_size = replies.size();
        return reply_size;
    }


    /* 댓글 작성 관련 로직 */
    /*@PostMapping("/board/detail/{boardId}/reply")
    public String boardReply(@PathVariable Long boardId, @Valid ReplyForm replyForm, @CurrentUser Account account) {
        Board currentBoard = boardRepository.findByBid(boardId);
        replyService.saveReply(replyForm, account, currentBoard);
        return "redirect:/board/detail/{boardId}";
    }*/

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
