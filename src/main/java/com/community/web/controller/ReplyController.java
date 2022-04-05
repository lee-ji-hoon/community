package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.infra.alarm.ReplyCreatePublish;
import com.community.domain.board.Board;
import com.community.domain.board.Reply;
import com.community.web.dto.ReplyForm;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.ReplyRepository;
import com.community.service.ReplyService;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final ApplicationEventPublisher applicationEventPublisher;

    private final ReplyService replyService;

    private String updatePath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

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
            Reply reply = replyService.saveReply(replyForm, account, currentBoard);

            Account writer = currentBoard.getWriter();
            log.info("manager : {}",writer.getId());
            log.info("account : {}",currentAccount.get().getId());

            if(!writer.getId().equals(currentAccount.get().getId())) {
                log.info("board 댓글 알림 이벤트 실행");
                applicationEventPublisher.publishEvent(new ReplyCreatePublish(reply, account));
            }

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
    @ResponseBody
    @RequestMapping(value = "/reply/delete")
    public String replyDelete(@RequestParam(value = "reply_delete_rid") Long reply_delete_rid) throws IOException{
        log.info("삭제 요청 : " + reply_delete_rid);
        String r_del_message = "";
        Reply findReply = replyRepository.findByRid(reply_delete_rid);
//        Board board = boardRepository.findByBid(findReply.getBoard().getBid());
//        String path = String.valueOf(board.getBid());

        if (findReply.getIsReported() || findReply.getReportCount() > 0) {
            log.info("이미 신고된 댓글");
            r_del_message = "<div class=\"bg-red-500 border p-4 relative rounded-md\" uk-alert id=\"isDeleted\">\n" +
                    "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                    "        <i class=\"icon-feather-x\"></i>\n" +
                    "    </button>\n" +
                    "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                    "    <p class=\"text-white text-opacity-75\">신고된 댓글은 삭제할 수 없습니다.</p>\n" +
                    "</div>";
            return r_del_message;
        }
        log.info("신고되지 않은 댓글");
        r_del_message = "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isDeleted\">\n" +
                "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                "        <i class=\"icon-feather-x\"></i>\n" +
                "    </button>\n" +
                "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                "    <p class=\"text-white text-opacity-75\">댓글이 삭제되었습니다.</p>\n" +
                "</div>";
        replyRepository.delete(findReply);
        return r_del_message;
    }

    /*@GetMapping("/board/detail/reply/delete/{rid}")
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
    }*/

}
