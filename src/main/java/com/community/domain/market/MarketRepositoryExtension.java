package com.community.domain.market;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MarketRepositoryExtension {

    Page<Market> findByMarketType(String marketType, Pageable pageable);
}
