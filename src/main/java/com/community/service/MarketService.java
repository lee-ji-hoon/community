package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.market.Market;
import com.community.domain.market.MarketItemStatus;
import com.community.domain.market.MarketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ModelMapper modelMapper;

    public Market createNewItem(Market market, Account account, String marketImagePath, String uploadFile, String uploadFolder) {
        market.setItemUploadTime(LocalDateTime.now());
        market.setMarketItemStatus(MarketItemStatus.판매중);
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
                log.info("marketType 판매 완료 변경");
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

    public void updateMarket(Market market, Account account) {
        marketRepository.save(market);
    }
}
