package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.market.Market;
import com.community.domain.market.MarketItemStatus;
import com.community.domain.market.MarketRepository;
import com.community.web.dto.MarketForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;

    public void createNewItem() {
        /*marketSetType(market, marketType);
        market.setUploadTime(LocalDateTime.now());
        market.setSeller(account);
        market.setFileName(uploadFolder+uploadFile);
        market.setFilePath(marketImagePath);
        return marketRepository.save(market);*/

    }

    public Market createNewItemNoImage(Market market, Account account, String marketType) {
        marketSetType(market, marketType);
        market.setUploadTime(LocalDateTime.now());
        market.setSeller(account);

        return marketRepository.save(market);
    }

    private void marketSetType(Market market, String marketType) {
        switch (marketType) {
            case "sell" :
                market.setMarketItemStatus(MarketItemStatus.SELLING);
                break;
            case "buy":
                market.setMarketItemStatus(MarketItemStatus.PURCHASE);
                break;
            case "share":
                market.setMarketItemStatus(MarketItemStatus.SHARE);
                break;
        }
    }

    public void updateMarketItemType(Market market, String marketType) {
        log.info("marketType : {}", marketType);
        switch (marketType){
            case "selling" :
                market.setMarketItemStatus(MarketItemStatus.SELLING);
                log.info("marketType 판매 중으로 변경");
                break;
            case "sold-out":
                market.setMarketItemStatus(MarketItemStatus.SOLDOUT);
                market.setPublished(false);
                log.info("marketType 판매 완료 변경");
                break;
            case "purchase" :
                market.setMarketItemStatus(MarketItemStatus.PURCHASE);
                log.info("marketType 구매중으로 변경");
                break;
            case "purchase-end":
                market.setMarketItemStatus(MarketItemStatus.COMPLETEPURCHASE);
                market.setPublished(false);
                log.info("marketType 구매 완료 변경");
                break;
            case "share" :
                market.setMarketItemStatus(MarketItemStatus.SHARE);
                log.info("marketType 나눔 중으로 변경");
                break;
            case "share-end":
                market.setMarketItemStatus(MarketItemStatus.COMPLETESHARE);
                market.setPublished(false);
                log.info("marketType 나눔 완료 변경");
                break;
        }
        marketRepository.save(market);

    }

    public void deleteProduct(Market market) {
        marketRepository.deleteById(market.getMarketId());
    }

    public void updateMarketImage(Market market, String marketImagePath, String uploadFile, String uploadFolder) {
        market.setFilePath(marketImagePath);
        market.setFileName(uploadFolder + uploadFile);

        marketRepository.save(market);
    }

    public void updateMarket(MarketForm marketForm, Market market, Account account, String marketType) {
        marketSetType(market, marketType);

        market.setPrice(marketForm.getPrice());
        market.setItemDetail(marketForm.getItemDetail());
        market.setItemName(marketForm.getItemName());
        market.setItemStatus(marketForm.getItemStatus());
        market.setMarketType(marketForm.getMarketType());

        marketRepository.save(market);
    }
}
