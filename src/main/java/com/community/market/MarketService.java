package com.community.market;

import com.community.account.entity.Account;
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

    public void createNewItem(Market market, Account account) {
        market.setItemUploadTime(LocalDateTime.now());
        market.setAccount(account);
        market.setMarketItemStatus(MarketItemStatus.SELLING);
        marketRepository.save(market);
    }
}
