package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.account.AccountType;
import com.community.domain.account.CurrentUser;
import com.community.domain.market.Market;
import com.community.domain.market.MarketRepository;
import com.community.domain.report.ReplyReport;
import com.community.domain.report.ReplyReportRepository;
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
    private final MarketRepository marketRepository;
    private final CouncilRepository councilRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ReplyReportRepository replyReportRepository;

    private final ReplyService replyService;

    private String updatePath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    @ResponseBody
    @RequestMapping(value = "/reply/add")
    public void addReplyAjax(@RequestParam(value = "r_postId") Long r_postId,
                             @RequestParam(value = "r_postSort") String r_postSort,
                             @RequestParam(value = "r_content") String r_content,
                             @CurrentUser Account account) {
        Reply reply = replyService.addReply(account, r_postSort, r_postId, r_content);
        switch (r_postSort) {
            case "board":
                Board currentBoard = boardRepository.findByBid(r_postId);
                if(!currentBoard.getWriter().getId().equals(account.getId())) {
                    log.info("board 댓글 알림 이벤트 실행");
                    applicationEventPublisher.publishEvent(new ReplyCreatePublish(reply, account));
                }
                break;
            case "council":
                Council currentCouncil = councilRepository.findByCid(r_postId);
                if(!currentCouncil.getPostWriter().getId().equals(account.getId())) {
                    log.info("board 댓글 알림 이벤트 실행");
                    applicationEventPublisher.publishEvent(new ReplyCreatePublish(reply, account));
                }
                break;
            case "market":
                Market currentMarket = marketRepository.findByMarketId(r_postId);
                if(!currentMarket.getSeller().getId().equals(account.getId())) {
                    log.info("board 댓글 알림 이벤트 실행");
                    applicationEventPublisher.publishEvent(new ReplyCreatePublish(reply, account));
                }
                break;
        }
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
    public String replyDelete(@RequestParam(value = "reply_delete_rid") Long reply_delete_rid,
                              @CurrentUser Account account) throws IOException{
        log.info("삭제 요청 : " + reply_delete_rid);
        String r_del_message = "";
        Reply findReply = replyRepository.findByRid(reply_delete_rid);

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
        if (findReply.getAccount().getNickname().equals(account.getNickname()) || account.getAccountType().equals(AccountType.ROLE_ADMIN)) {
            log.info("신고되지 않은 댓글");
            r_del_message = "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isDeleted\">\n" +
                    "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                    "        <i class=\"icon-feather-x\"></i>\n" +
                    "    </button>\n" +
                    "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                    "    <p class=\"text-white text-opacity-75\">댓글이 삭제되었습니다.</p>\n" +
                    "</div>";
            replyRepository.delete(findReply);
        }
        return r_del_message;
    }
}
