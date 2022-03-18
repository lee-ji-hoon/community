package com.community.market;

import com.community.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {

    Market findByMarketId(Long id);

    List<Market> findAllByMarketTypeOrderByItemUploadTimeDesc(String type);

    /* 검색 쿼리 */
    List<Market> findByItemNameContainingOrderByItemUploadTimeDesc(String keyword);
    List<Market> findByItemDetailContainingOrderByItemUploadTimeDesc(String keyword);

    List<Market> findAllBySeller(Account seller);
}
