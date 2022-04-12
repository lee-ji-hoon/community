package com.community.domain.market;

import com.community.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long>, MarketRepositoryExtension {

    Market findByMarketId(Long id);

    // 프로필 용 마켓 count
    long countAllBySellerAndMarketType(Account account, String type);

    /* 검색 쿼리 */
    List<Market> findByItemNameContainingOrderByUploadTimeDesc(String keyword);
    List<Market> findByItemDetailContainingOrderByUploadTimeDesc(String keyword);

}
