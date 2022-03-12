package com.community.market;

import com.community.account.AccountService;
import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    private final MarketService marketService;
    private final ModelMapper modelMapper;
    private final MarketRepository marketRepository;

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

    @GetMapping("/market/{marketId}")
    public String marketDetail(@CurrentUser Account account, Model model,
                               @PathVariable long marketId) {

        model.addAttribute(account);
        model.addAttribute("product", marketRepository.findByMarketId(marketId));

        return "market/market-detail";
    }
}
