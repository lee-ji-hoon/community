package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.account.CurrentUser;
import com.community.domain.chat.Chat;
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
import java.util.List;
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
        List<Chat> sendChatLists = chatRepository.findBySenderOrderBySendTimeDesc(account);
        for (Chat sendChatList : sendChatLists) {
            log.info(sendChatList.getReceiver().getNickname());
        }
        model.addAttribute(sendChatLists);
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
    public String addMarketReplyUpdate(@RequestParam(value = "c_marketId") Long c_marketId,
                                    @RequestParam(value = "c_receiver") Long c_receiver,
                                    @RequestParam(value = "c_content") String c_content,
                                    ChatForm chatForm, @CurrentUser Account account) throws IOException {
        Optional<Account> receiver = accountRepository.findById(c_receiver);
        Optional<Chat> existChat = chatRepository.findBySenderAndReceiver(account, receiver.get());
        if (existChat.isEmpty()) {
            chatForm.setReceiver(c_receiver);
            chatForm.setMarketId(c_marketId);
            chatForm.setContent(c_content);
            chatService.saveMarketChat(chatForm, account);
        }
        if (existChat.isPresent()) {
            Long roomId = existChat.get().getRoom();
            chatForm.setReceiver(c_receiver);
            chatForm.setMarketId(c_marketId);
            chatForm.setContent(c_content);
            chatService.addChat(roomId, chatForm, account);
        }

        String sendChat = "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                "        <i class=\"icon-feather-x\"></i>\n" +
                "    </button>\n" +
                "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                "    <p class=\"text-white text-opacity-75\">쪽지를 보냈습니다.</p>\n" +
                "</div>";


        return sendChat;
    }
}
