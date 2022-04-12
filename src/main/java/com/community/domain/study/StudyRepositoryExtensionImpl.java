package com.community.domain.study;

import com.community.domain.account.Account;
import com.community.domain.market.Market;
import com.community.domain.tag.QTag;
import com.community.domain.tag.Tag;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.community.domain.study.QStudy.study;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public List<Study> findByAccount(Set<Tag> tags) {
        QStudy qStudy = QStudy.study;
        JPQLQuery<Study> studyJPQLQuery = from(qStudy).where(study.limitMemberDate.after(LocalDate.now())
                        .and(qStudy.tags.any().in(tags)))
                        .leftJoin(qStudy.tags, QTag.tag).fetchJoin()
                        .orderBy(qStudy.publishedDateTime.desc())
                        .distinct()
                        .limit(9);
        return studyJPQLQuery.fetch();

    }

    @Override
    public Page<Study> findByMembersNotContaining(Account account, Pageable pageable) {
        QStudy qStudy = QStudy.study;
        JPQLQuery<Study> query = from(qStudy).where(study.limitMemberDate.after(LocalDate.now()))
                        .distinct();

        JPQLQuery<Study> studyJPQLQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Study> studyQueryResults = studyJPQLQuery.fetchResults();

        return new PageImpl<>(studyQueryResults.getResults(), pageable, studyQueryResults.getTotal());
    }
}
