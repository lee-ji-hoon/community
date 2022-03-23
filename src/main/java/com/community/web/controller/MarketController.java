package com.community.web.controller;

import com.community.domain.account.CurrentUser;
import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.board.Reply;
import com.community.web.dto.ReplyForm;
import com.community.domain.board.ReplyRepository;
import com.community.service.BoardService;
import com.community.service.ReplyService;
import com.community.domain.market.Market;
import com.community.domain.market.MarketRepository;
import com.community.service.MarketService;
import com.community.web.dto.MarketForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    private final MarketService marketService;
    private final ModelMapper modelMapper;
    private final BoardService boardService;
    private final ReplyService replyService;

    private final MarketRepository marketRepository;
    private final ReplyRepository replyRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/market")
    public String marketListView(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new MarketForm());
        model.addAttribute("sellingProduct", marketRepository.findAllByMarketTypeOrderByItemUploadTimeDesc("판매"));
        model.addAttribute("buyProduct", marketRepository.findAllByMarketTypeOrderByItemUploadTimeDesc("구매"));
        model.addAttribute("shareProduct", marketRepository.findAllByMarketTypeOrderByItemUploadTimeDesc("나눔"));
        model.addAttribute("myProduct", marketRepository.findAllBySeller(account));

        return "market/market-list";
    }

    @PostMapping("/market/new")
    public String marketProductForm(@CurrentUser Account account, Model model,
                                    @Valid MarketForm marketForm) {

        Market newItem = marketService.createNewItem(modelMapper.map(marketForm, Market.class), account);

        model.addAttribute(account);
        Long marketId = newItem.getMarketId();
        return "redirect:/market/" + marketId;
    }

    @ResponseBody
    @RequestMapping(value = "/market/update")
    public String marketUpdate(MarketForm marketForm, @CurrentUser Account account,
                               @RequestParam(value = "bid") String bid,
                               @RequestParam(value = "marketType") String marketType,
                               @RequestParam(value = "itemName") String itemName,
                               @RequestParam(value = "price") int price,
                               @RequestParam(value = "itemDetail") String itemDetail){
        log.info("market update 실행");
        Long marketNum = Long.valueOf(bid);
        Market byMarketId = marketRepository.findByMarketId(marketNum);
        String message = null;
        if (account.getId().equals(byMarketId.getSeller().getId())) {
            marketService.updateMarket(byMarketId, marketForm);
            /*marketForm.setMarketType(marketType);
            marketForm.setItemName(itemName);
            marketForm.setPrice(price);
            marketForm.setItemDetail(itemDetail);*/
//            marketService.updateMarket(boardId, marketForm);
            message = "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                    "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                    "        <i class=\"icon-feather-x\"></i>\n" +
                    "    </button>\n" +
                    "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                    "    <p class=\"text-white text-opacity-75\">게시물이 수정되었습니다.</p>\n" +
                    "</div>";
            return message;
        }
        log.info("잘못된 게시물 수정 요청 : bid = " + byMarketId + " accountId = " + account.getId());
        message = "잘못된 요청입니다.";
        return message;
    }

    @GetMapping("/market/{marketId}")
    public String marketDetail(@CurrentUser Account account, Model model,
                               @PathVariable long marketId) {

        Market detail = marketRepository.findByMarketId(marketId);
        List<Reply> replies = replyRepository.findAllByMarketOrderByUploadTimeDesc(detail);

        model.addAttribute(account);
        model.addAttribute("product", detail);
        model.addAttribute("reply", replies);
        model.addAttribute("service", boardService);
        model.addAttribute(new MarketForm());

        return "market/market-detail";
    }

    @GetMapping("/market/{marketId}/delete")
    public String marketDelete(@CurrentUser Account account, Model model,
                               @PathVariable long marketId, RedirectAttributes redirectAttributes) {
        Market byMarketId = marketRepository.findByMarketId(marketId);
        if (account.getId().equals(byMarketId.getSeller().getId())) {
            marketRepository.delete(byMarketId);
            redirectAttributes.addFlashAttribute("message", "해당 게시글이 삭제 됐습니다.");
            return "redirect:/market";
        }
        return "error-page";
    }

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
        Market byMarketId = marketRepository.findByMarketId(reply.getMarket().getMarketId());
        List<Reply> replies = replyRepository.findAllByMarket(byMarketId);
        int reply_size = replies.size();
        return reply_size;
    }

    @GetMapping("/market/reply/delete/{rid}")
    public String marketReplyDelete(@PathVariable Long rid,
                                    RedirectAttributes redirectAttributes) {
        Reply findReply = replyRepository.findByRid(rid);
        Market byMarketId = marketRepository.findByMarketId(findReply.getMarket().getMarketId());

        redirectAttributes.addFlashAttribute("r_del_complete_message", "댓글이 삭제되었습니다.");
        replyRepository.delete(findReply);
        return "redirect:/market/" + byMarketId.getMarketId();
    }

    // 중고거래 댓글 추가 끝
}