package com.community.market;

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

    public void createNewItem(Market market) {
        System.out.println("MarketService.createNewItem");
        market.setItemUploadTime(LocalDateTime.now());
        marketRepository.save(market);
    }
}
