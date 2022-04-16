package com.community.domain.market;

import com.community.domain.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MarketRepositoryExtension {

    Page<Market> findByMarketType(String marketType, Pageable pageable);

    Page<Market> findBySeller(Account account, Pageable pageable);

    Page<Market> findBySellerAndMarketType(Account account, String marketType, Pageable pageable);

    Page<Market> findByKeywordAndType(String keyword, String type, Pageable pageable);
}
