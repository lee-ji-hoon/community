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
import com.community.web.dto.MarketForm;
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
                Page<Market> marketTypeSell = marketRepository.findByMarketType("판매", pageable);
                model.addAttribute("marketType", marketTypeSell);
                break;
            case "buy" : // 구매
                Page<Market> marketTypeBuy = marketRepository.findByMarketType("구매", pageable);
                model.addAttribute("marketType", marketTypeBuy);
                break;
            case "share" : // 나눔
                Page<Market> marketTypeShare = marketRepository.findByMarketType("나눔", pageable);
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

    @GetMapping("/market/register/new")
    public String marketNewForm(@CurrentUser Account account, Model model, @Valid MarketForm marketForm, RedirectAttributes redirectAttributes){
        boolean emailVerified = account.isEmailVerified();

        log.info("email 체크 : {}", emailVerified);
        if (!emailVerified) {
            model.addAttribute(account);
            redirectAttributes.addFlashAttribute("emailVerifiedChecked", "이메일 인증 후에 사용 가능합니다.");

            return "redirect:/market/";
        }

        model.addAttribute(account);
        model.addAttribute(new MarketForm());

        return "market/market-form";
    }

    @PostMapping("/market/register/new")
    public String marketNewProduct(@CurrentUser Account account, Model model,
                                    @Valid MarketForm marketForm,
                                    @RequestPart MultipartFile file,
                                    @RequestParam("imageFile") String imageFile) throws IOException {
        log.info("file = {}", file.getName());
        log.info("imageFile = {}", imageFile);
        Long marketId = null;

        if (Objects.equals(imageFile, "checked")) {  // 이미지가 존재
            String folderPath = "market-img/";

            String uploadFile = s3Service.upload(file, folderPath);
            /*s3에 직접 접근*/
            /*String marketImagePath = "https://"+bucket+".s3.ap-northeast-2.amazonaws.com/"+uploadFile;*/

            /*CloudFront에로 s3에 접근*/
            String marketImagePath = S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + folderPath + uploadFile;

            Market newItem = marketService.createNewItem(modelMapper.map(marketForm, Market.class),
                                                                        account, marketImagePath, uploadFile, folderPath, marketForm.getMarketType());
            model.addAttribute(account);
            marketId = newItem.getMarketId();

        } else { // 이미지 없음
            Market newItem = marketService.createNewItemNoImage(modelMapper.map(marketForm, Market.class), account, marketForm.getMarketType());
            model.addAttribute(account);
            marketId = newItem.getMarketId();
        }
        return "redirect:/market/detail/" + marketId;
    }

    @PostMapping(value = "/market/detail/{marketId}/update")
    public String marketUpdate(@CurrentUser Account account, @PathVariable long marketId,
                             @Valid MarketForm marketForm,
                             @RequestPart MultipartFile file,
                             @RequestParam("imageFile") String imageFile) throws IOException {

        log.info("market getMarketType update : {}", marketForm.getMarketType());
        log.info("market getItemStatus update : {}", marketForm.getItemStatus());
        Market market = marketRepository.findByMarketId(marketId);
        if (account.getId().equals(market.getSeller().getId())) { // 현재 접속중 유저와 seller 동일 체크
            if (Objects.equals(imageFile, "checked")) {  // 새로운 이미지가 존재
                s3Service.deleteFile(market.getFileName());
                String uploadFolder = "market-img/"; // 업로드 폴더
                String uploadFile = s3Service.upload(file, uploadFolder); // 이미지 업로드

                /*CloudFront에로 s3에 접근*/
                String marketImagePath = S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + uploadFolder + uploadFile;

                marketService.updateMarketImage(market ,marketImagePath, uploadFile, uploadFolder);
            }
            marketService.updateMarket(marketForm, market, account);
        }
        return "redirect:/market/detail/" + marketId;
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
        model.addAttribute(new MarketForm(detail));

        return "market/market-detail";
    }

    @PostMapping("/market/detail/{marketId}/delete")
    public String marketDelete(@CurrentUser Account account, Model model,
                               @PathVariable long marketId, RedirectAttributes redirectAttributes) {
        Market market = marketRepository.findByMarketId(marketId);

        if (account.getId().equals(market.getSeller().getId())) {
            String fileName = market.getFileName();
            if (fileName != null) s3Service.deleteFile(fileName);

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
