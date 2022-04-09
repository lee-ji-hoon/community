package com.community.domain.market;

import com.community.domain.account.Account;
import com.community.domain.market.Market;
import com.community.domain.study.StudyRepositoryExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long>, MarketRepositoryExtension {

    Market findByMarketId(Long id);

    List<Market> findAllByMarketTypeOrderByItemUploadTimeDesc(String type);

    /* 검색 쿼리 */
    List<Market> findByItemNameContainingOrderByItemUploadTimeDesc(String keyword);
    List<Market> findByItemDetailContainingOrderByItemUploadTimeDesc(String keyword);

    List<Market> findAllBySeller(Account seller);
}
