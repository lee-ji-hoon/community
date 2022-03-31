package com.community.web.controller;

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
public class InfoPageController {

    @GetMapping("/info/about")
    public String aboutPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-about";
    }
    @GetMapping("/info/contact")
    public String contactPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-contact";
    }
    @GetMapping("/info/privacy")
    public String privacyPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "info/info-privacy";
    }
}
