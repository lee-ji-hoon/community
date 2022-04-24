package com.community.web.controller;

import com.community.domain.account.CurrentUser;
import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import com.community.domain.bookmark.Bookmark;
import com.community.domain.bookmark.BookmarkRepository;
import com.community.infra.aws.S3Service;
import com.community.domain.board.ReplyRepository;
import com.community.service.BoardService;
import com.community.domain.market.Market;
import com.community.domain.market.MarketRepository;
import com.community.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    private final MarketService marketService;
    private final ModelMapper modelMapper;
    private final BoardService boardService;
    private final S3Service s3Service;

    private final MarketRepository marketRepository;
    private final ReplyRepository replyRepository;
    private final BookmarkRepository bookmarkRepository;

    @GetMapping("/market/{type}")
    public String marketListView(@CurrentUser Account account, Model model, @PathVariable String type,
                                 @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                                    direction = Sort.Direction.ASC) Pageable pageable,
                                 @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

        switch (type) {
            case "sell" : // 판매
                Page<Market> marketTypeSell = marketRepository.findByMarketType("sell", pageable);
                model.addAttribute("marketType", marketTypeSell);
                break;
            case "buy" : // 구매
                Page<Market> marketTypeBuy = marketRepository.findByMarketType("buy", pageable);
                model.addAttribute("marketType", marketTypeBuy);
                break;
            case "share" : // 나눔
                Page<Market> marketTypeShare = marketRepository.findByMarketType("share", pageable);
                model.addAttribute("marketType", marketTypeShare);
                break;
            case "myProduct" : // 내 물건
                Page<Market> myProduct = marketRepository.findBySeller(account, pageable);
                model.addAttribute("marketType", myProduct);
                break;
        }

        model.addAttribute(account);
        model.addAttribute(type);
        model.addAttribute("pageNo", page);

        return "market/market-list";
    }

    @GetMapping("/market/{type}/search")
    public String searchMarket(String keyword, Model model, @PathVariable String type,
                               @CurrentUser Account account,
                               @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                       direction = Sort.Direction.ASC) Pageable pageable,
                               @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

        Page<Market> searchMarketResult = marketRepository.findByKeywordAndType(keyword, type, pageable);

        for (Market market : searchMarketResult) {
            log.info("market 페이지 : {}", market);
        }

        model.addAttribute(account);
        model.addAttribute("searchMarketResult", searchMarketResult);
        model.addAttribute("pageNo", page);
        model.addAttribute("keyword", keyword);

        return "market/market-search";

    }

    @GetMapping("/market/register/new")
    public String marketNewForm(@CurrentUser Account account, Model model,  RedirectAttributes redirectAttributes){
        boolean emailVerified = account.isEmailVerified();

        log.info("email 체크 : {}", emailVerified);
        if (!emailVerified) {
            model.addAttribute(account);
            redirectAttributes.addFlashAttribute("emailVerifiedChecked", "이메일 인증 후에 사용 가능합니다.");

            return "redirect:/market/";
        }

        model.addAttribute(account);

        return "market/market-form";
    }
    @ResponseBody
    @RequestMapping(value = "/market/register/new", method = RequestMethod.POST)
    public String marketNewProduct(@CurrentUser Account account,
                                    @RequestPart(value = "article_file", required = false) List<MultipartFile> multipartFileLIst,
                                    @RequestPart(value = "itemName", required = false) String itemName,
                                    @RequestPart(value = "description", required = false) String description,
                                    @RequestPart(value = "price", required = false) int price,
                                    @RequestPart(value = "itemStatus", required = false) String itemStatus) throws IOException {

        Market newItem = marketService.createNewItem(multipartFileLIst, itemName, description, price, itemStatus, account);


        return "redirect:/market/" + newItem.getMarketId();
    }

    @GetMapping("/market/detail/{marketId}")
    public String marketDetail(@CurrentUser Account account, Model model,
                               @PathVariable long marketId) {

        Market detail = marketRepository.findByMarketId(marketId);
        List<Reply> replies = replyRepository.findAllByMarketOrderByUploadTimeDesc(detail);

        Optional<Bookmark> existBookmark = bookmarkRepository.findByAccountAndMarket(account, detail);

        model.addAttribute("bookmark", existBookmark);
        model.addAttribute(account);
        model.addAttribute("product", detail);
        model.addAttribute("reply", replies);
        model.addAttribute("service", boardService);

        return "market/market-detail";
    }

    @PostMapping("/market/detail/{marketId}/delete")
    public String marketDelete(@CurrentUser Account account, Model model,
                               @PathVariable Long marketId, RedirectAttributes redirectAttributes) {
        Market market = marketRepository.findByMarketId(marketId);

        if (account.getId().equals(market.getSeller().getId())) {
            marketService.deleteProduct(market);
            redirectAttributes.addFlashAttribute("message", "해당 게시글이 삭제 됐습니다.");
            model.addAttribute(account);
            return "redirect:/market/sell";
        }
        return "error-page";
    }

    @ResponseBody
    @RequestMapping(value = "/market/detail/{marketId}/type/{status}")
    public void marketStatusUpdate(@PathVariable long marketId, @PathVariable String status) throws IOException {
        Market market = marketRepository.findByMarketId(marketId);

        marketService.updateMarketItemType(market, status);

    }
}
