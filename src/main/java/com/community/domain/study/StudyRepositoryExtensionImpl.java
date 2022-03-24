package com.community.domain.study;

import com.community.domain.tag.QTag;
import com.community.domain.tag.Tag;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.community.domain.study.QStudy.study;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {


    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     */
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
}
