package com.community.chat;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    @GetMapping("/chat/lists")
    public String chatLists(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "chat/chat-detail";
    }
}
