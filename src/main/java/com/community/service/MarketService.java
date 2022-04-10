package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.market.Market;
import com.community.domain.market.MarketItemStatus;
import com.community.domain.market.MarketRepository;
import com.community.web.dto.MarketForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;

    public Market createNewItem(Market market, Account account, String marketImagePath, String uploadFile, String uploadFolder, String marketType) {
        switch (marketType) {
            case "판매" :
                market.setMarketItemStatus(MarketItemStatus.판매중);
                break;
            case "구매":
                market.setMarketItemStatus(MarketItemStatus.구매);
                break;
            case "나눔":
                market.setMarketItemStatus(MarketItemStatus.나눔);
                break;
        }
        market.setItemUploadTime(LocalDateTime.now());
        market.setSeller(account);
        market.setFileName(uploadFolder+uploadFile);
        market.setFilePath(marketImagePath);

        return marketRepository.save(market);
    }

    public Market createNewItemNoImage(Market market, Account account) {
        market.setItemUploadTime(LocalDateTime.now());
        market.setMarketItemStatus(MarketItemStatus.판매중);
        market.setSeller(account);

        return marketRepository.save(market);
    }

    public void updateMarketItemType(Market market, String marketType) {
        log.info("marketType : {}", marketType);
        switch (marketType){
            case "selling" :
                market.setMarketItemStatus(MarketItemStatus.판매중);
                log.info("marketType 판매 중으로 변경");
                break;
            case "sold-out":
                market.setMarketItemStatus(MarketItemStatus.판매완료);
                market.setPublished(false);
                log.info("marketType 판매 완료 변경");
                break;
            case "purchase" :
                market.setMarketItemStatus(MarketItemStatus.구매);
                log.info("marketType 구매중으로 변경");
                break;
            case "purchase-end":
                market.setMarketItemStatus(MarketItemStatus.구매완료);
                market.setPublished(false);
                log.info("marketType 구매 완료 변경");
                break;
            case "share" :
                market.setMarketItemStatus(MarketItemStatus.나눔);
                log.info("marketType 나눔 중으로 변경");
                break;
            case "share-end":
                market.setMarketItemStatus(MarketItemStatus.나눔완료);
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

    public void updateMarket(MarketForm marketForm, Market market, Account account) {
        market.setPrice(marketForm.getPrice());
        market.setItemDetail(marketForm.getItemDetail());
        market.setItemName(marketForm.getItemName());
        market.setItemStatus(marketForm.getItemStatus());
        market.setMarketType(marketForm.getMarketType());

        marketRepository.save(market);
    }
}
