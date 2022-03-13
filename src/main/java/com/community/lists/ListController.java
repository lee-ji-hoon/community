package com.community.lists;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ListController {

    @GetMapping("/lists")
    public String listPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "lists/lists";
    }
}
