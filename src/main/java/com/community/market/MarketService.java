package com.community.market;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.form.BoardForm;
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

    public Market createNewItem(Market market, Account account) {
        market.setItemUploadTime(LocalDateTime.now());
        market.setMarketItemStatus(MarketItemStatus.판매중);
        market.setSeller(account);
        return marketRepository.save(market);
    }


    public void updateMarket(Market byMarketId, MarketForm marketForm) {
        modelMapper.map(marketForm, byMarketId);
    }
}
