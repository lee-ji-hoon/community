package com.community.market;

import com.community.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {

    Market findByMarketId(Long id);

    List<Market> findAllByMarketTypeOrderByItemUploadTimeDesc(String type);

    List<Market> findAllBySeller(Account seller);
}
