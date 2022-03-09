package com.community.market;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MarketController {

    @GetMapping("/market")
    public String marketView(@CurrentUser Account account, Model model) {
        model.addAttribute(account);

        return "market/market-list";
    }
}
