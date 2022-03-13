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
import com.community.council.Council;
import com.community.council.CouncilRepository;
import com.community.like.Likes;
import com.community.market.Market;
import com.community.market.MarketRepository;
import com.community.study.entity.Meetings;
import com.community.study.repository.MeetingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReplyController {

    private final BoardRepository boardRepository;
    private final AccountRepository accountRepository;
    private final ReplyRepository replyRepository;
    private final CouncilRepository councilRepository;
    private final MeetingsRepository meetingsRepository;
    private final MarketRepository marketRepository;

    private final ReplyService replyService;

    private String updatePath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    // 모임 댓글 추가 시작
    @ResponseBody
    @RequestMapping(value = "/study/meetings/reply")
    public int addMeetingReplyLink(@RequestParam(value = "r_board_id") Long r_board_id,
                                   @RequestParam(value = "r_account_id") Long r_account_id,
                                   @RequestParam(value = "r_content") String r_content,
                                   ReplyForm replyForm) throws IOException {
        log.info("댓글 작성 호출");
        log.info(r_board_id + "r_board_id");
        log.info(r_account_id + "r_account_id");
        log.info(r_content + "r_content");

        replyForm.setContent(r_content);
        Meetings currentMeetings = meetingsRepository.findByMeetingsId(r_board_id);
        Optional<Account> currentAccount = accountRepository.findById(r_account_id);
        if (currentAccount.isPresent()) {
            String accountEmail = currentAccount.get().getEmail();
            Account account = accountRepository.findByEmail(accountEmail);
            replyService.saveMeetingsReply(replyForm, account, currentMeetings);
            List<Reply> replies = replyRepository.findAll();
            int reply_size = replies.size();
            return reply_size;
        }
        List<Reply> replies = replyRepository.findAll();
        int reply_size = replies.size();
        return reply_size;
    }

    @ResponseBody
    @RequestMapping(value = "/study/meetings/reply/update")
    public int addMeetingReplyUpdate(@RequestParam(value = "reply_update_rid") Long reply_update_rid,
                                     @RequestParam(value = "reply_update_content") String reply_update_content) throws IOException{
        log.info("rid : " + reply_update_rid);
        log.info("content : " + reply_update_content);
        replyService.updateReply(reply_update_rid, reply_update_content);

        Reply reply = replyRepository.findByRid(reply_update_rid);
        Meetings byMeetingsId = meetingsRepository.findByMeetingsId(reply.getMeetings().getMeetingsId());
        List<Reply> replies = replyRepository.findAllByMeetings(byMeetingsId);
        int reply_size = replies.size();
        return reply_size;
    }
    // 모임 댓글 추가 끝

    // 중고거래 댓글 추가 시작
    @ResponseBody
    @RequestMapping(value = "/market/reply")
    public int addMarketReply(@RequestParam(value = "r_board_id") Long r_board_id,
                              @RequestParam(value = "r_account_id") Long r_account_id,
                              @RequestParam(value = "r_content") String r_content,
                              ReplyForm replyForm) throws IOException {
        log.info("댓글 작성 호출");
        log.info(r_board_id + "r_board_id");
        log.info(r_account_id + "r_account_id");
        log.info(r_content + "r_content");

        replyForm.setContent(r_content);
        Market byMarketId = marketRepository.findByMarketId(r_board_id);
        Optional<Account> currentAccount = accountRepository.findById(r_account_id);
        if (currentAccount.isPresent()) {
            String accountEmail = currentAccount.get().getEmail();
            Account account = accountRepository.findByEmail(accountEmail);
            replyService.saveMarketReply(replyForm, account, byMarketId);
            List<Reply> replies = replyRepository.findAll();
            int reply_size = replies.size();
            return reply_size;
        }
        List<Reply> replies = replyRepository.findAll();
        int reply_size = replies.size();
        return reply_size;
    }

    @ResponseBody
    @RequestMapping(value = "/market/reply/update")
    public int addMarketReplyUpdate(@RequestParam(value = "reply_update_rid") Long reply_update_rid,
                                    @RequestParam(value = "reply_update_content") String reply_update_content) throws IOException{
        log.info("rid : " + reply_update_rid);
        log.info("content : " + reply_update_content);
        replyService.updateReply(reply_update_rid, reply_update_content);

        Reply reply = replyRepository.findByRid(reply_update_rid);
        Market byMarketId = marketRepository.findByMarketId(reply.getMeetings().getMeetingsId());
        List<Reply> replies = replyRepository.findAllByMarket(byMarketId);
        int reply_size = replies.size();
        return reply_size;
    }
    // 중고거래 댓글 추가 끝

    // 댓글 관련 내용
    @ResponseBody
    @RequestMapping(value = "/board/detail/reply")
    public int addBoardReplyLink(@RequestParam(value = "r_board_id") Long r_board_id,
                           @RequestParam(value = "r_account_id") Long r_account_id,
                           @RequestParam(value = "r_content") String r_content,
                           ReplyForm replyForm) throws IOException {
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



    @ResponseBody
    @RequestMapping(value = "/council/detail/reply")
    public int addCouncilReplyLink(@RequestParam(value = "r_council_id") Long r_council_id,
                           @RequestParam(value = "r_account_id") Long r_account_id,
                           @RequestParam(value = "r_content") String r_content,
                           ReplyForm replyForm) throws IOException {
        log.info("댓글 작성 호출");
        log.info(r_council_id + "r_council_id");
        log.info(r_account_id + "r_account_id");
        log.info(r_content + "r_content");
        replyForm.setContent(r_content);
        Council currentPost = councilRepository.findByCid(r_council_id);
        Optional<Account> currentAccount = accountRepository.findById(r_account_id);
        if (currentAccount.isPresent()) {
            String accountEmail = currentAccount.get().getEmail();
            Account account = accountRepository.findByEmail(accountEmail);
            replyService.saveCouncilReply(replyForm, account, currentPost);
            List<Reply> replies = replyRepository.findAll();
            int reply_size = replies.size();
            return reply_size;
        }
        List<Reply> replies = replyRepository.findAll();
        int reply_size = replies.size();
        return reply_size;
    }

    @ResponseBody
    @RequestMapping(value = "/reply/update")
    public int replyUpdate(@RequestParam(value = "reply_update_rid") Long reply_update_rid,
                                @RequestParam(value = "reply_update_content") String reply_update_content) throws IOException{
        log.info("rid : " + reply_update_rid);
        log.info("content : " + reply_update_content);
        replyService.updateReply(reply_update_rid, reply_update_content);

        int reply_size = 0;
        return reply_size;
    }

    @GetMapping("/board/detail/reply/delete/{rid}")
    public String boardReplyDelete(@PathVariable Long rid,
                                   RedirectAttributes redirectAttributes) {
        String r_del_error_message = null;
        Reply findReply = replyRepository.findByRid(rid);
        Board board = boardRepository.findByBid(findReply.getBoard().getBid());
        String path = String.valueOf(board.getBid());

        if (findReply.getIsReported() || findReply.getReportCount() > 0) {
            r_del_error_message = "신고된 댓글은 삭제할 수 없습니다.";
            return r_del_error_message;
        }
        redirectAttributes.addFlashAttribute("r_del_complete_message", "댓글이 삭제되었습니다.");
        replyRepository.delete(findReply);
        return "redirect:/board/detail/" + updatePath(path);
    }

}
