package com.community.domain.graduation;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class GraduationRepositoryExtensionImpl extends QuerydslRepositorySupport implements GraduationRepositoryExtension {

    public GraduationRepositoryExtensionImpl() {
        super(Graduation.class);
    }

    @Override
    public Page<Graduation> findAllGraduation(Pageable pageable) {
        QGraduation qGraduation = QGraduation.graduation;
        JPQLQuery<Graduation> query = from(qGraduation)
                .distinct();

        JPQLQuery<Graduation> graduationJPQLQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Graduation> graduationQueryResults = graduationJPQLQuery.fetchResults();

        return new PageImpl<>(graduationQueryResults.getResults(), pageable, graduationQueryResults.getTotal());
    }

    @Override
    public Page<Graduation> findByGraduationDate(int date, Pageable pageable) {
        QGraduation qGraduation = QGraduation.graduation;
        JPQLQuery<Graduation> query = from(qGraduation)
                .where(qGraduation.graduationDate.eq(date))
                .distinct();

        JPQLQuery<Graduation> graduationJPQLQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Graduation> graduationQueryResults = graduationJPQLQuery.fetchResults();

        return new PageImpl<>(graduationQueryResults.getResults(), pageable, graduationQueryResults.getTotal());
    }
}
