package com.community.market;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    private final MarketService marketService;
    private final ModelMapper modelMapper;

    @GetMapping("/market")
    public String marketListView(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new MarketForm());

        return "market/market-list";
    }

    @PostMapping("/market/new")
    public String marketProductForm(@CurrentUser Account account, Model model, MarketForm marketForm) {

        marketService.createNewItem(modelMapper.map(marketForm, Market.class), account);

        model.addAttribute(account);


        return "redirect:/market";
    }
}
