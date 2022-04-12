package com.community.domain.market;

import com.community.domain.account.Account;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

public class MarketRepositoryExtensionImpl extends QuerydslRepositorySupport implements MarketRepositoryExtension {

    public MarketRepositoryExtensionImpl() {
        super(Market.class);
    }

    @Override
    public Page<Market> findByMarketType(String marketType, Pageable pageable) {
        QMarket market = QMarket.market;

        JPQLQuery<Market> query = from(market).where(market.published.isTrue()
                .and(market.marketType.contains(marketType)))
                .distinct();

        JPQLQuery<Market> marketJPQLQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Market> marketQueryResults = marketJPQLQuery.fetchResults();

        return new PageImpl<>(marketQueryResults.getResults(), pageable, marketQueryResults.getTotal());
    }

    @Override
    public Page<Market> findBySeller(Account account, Pageable pageable) {
        QMarket market = QMarket.market;

        JPQLQuery<Market> query = from(market).where(market.published.isTrue()
                .and(market.seller.eq(account)))
                .distinct();

        JPQLQuery<Market> marketJPQLQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Market> marketQueryResults = marketJPQLQuery.fetchResults();

        return new PageImpl<>(marketQueryResults.getResults(), pageable, marketQueryResults.getTotal());
    }
}
