package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.account.CurrentUser;
import com.community.domain.chat.ChatRepository;
import com.community.domain.market.Market;
import com.community.domain.market.MarketRepository;
import com.community.service.ChatService;
import com.community.web.dto.ChatForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MarketRepository marketRepository;
    private final AccountRepository accountRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;

    @GetMapping("/chat/lists")
    public String chatLists(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "chat/chat-detail";
    }

    @GetMapping("/chat/new")
    public String createNewChat(@CurrentUser Account account, Model model) {
        return "";
    }

    // 장터 전용 쪽지 보내기
    @ResponseBody
    @RequestMapping(value = "/market/chat/new")
    public int addMarketReplyUpdate(@RequestParam(value = "c_marketId") Long c_marketId,
                                    @RequestParam(value = "c_receiver") Long c_receiver,
                                    @RequestParam(value = "c_content") String c_content,
                                    ChatForm chatForm, @CurrentUser Account account) throws IOException {
        chatForm.setReceiver(c_receiver);
        chatForm.setMarketId(c_marketId);
        chatForm.setContent(c_content);
        chatService.saveMarketChat(chatForm, account);


        return 1;
    }
}
