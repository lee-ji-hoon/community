package com.community.study;

import com.community.account.entity.Account;
import com.community.tag.Tag;
import com.community.zone.Zone;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long> {

    boolean existsByPath(String path);

    @EntityGraph(value = "Study.withAll", type = EntityGraph.EntityGraphType.LOAD)
    Study findByPath(String path);

    @EntityGraph(value = "Study.withTagsAndManagers", type = EntityGraph.EntityGraphType.FETCH)
    Study findAccountWithTagsByPath(String path);

    @EntityGraph(value = "Study.withZonesAndManagers", type = EntityGraph.EntityGraphType.FETCH)
    Study findAccountWithZonesByPath(String path);

    @EntityGraph(value = "Study.withManagers", type = EntityGraph.EntityGraphType.FETCH)
    Study findAccountWithManagersByPath(String path);

    @EntityGraph(value = "Study.withMembers", type = EntityGraph.EntityGraphType.FETCH)
    Study findStudyWithMembersByPath(String path);

    @EntityGraph(value = "Study.withZonesTags", type = EntityGraph.EntityGraphType.FETCH)
    List<Study> findFirst9ByPublishedAndClosedOrderByPublishedDateTimeDesc(boolean published, boolean closed);

    @EntityGraph(value = "Study.withZonesTags", type = EntityGraph.EntityGraphType.FETCH)
    List<Study> findFirst9ByPublishedAndClosedOrderByIdDesc(boolean published, boolean closed);

    @EntityGraph(value = "Study.withZonesTags", type = EntityGraph.EntityGraphType.FETCH)
    List<Study> findFirst5ByManagersContainingAndClosedOrderByPublishedDateTimeDesc(Account account, boolean closed);

    @EntityGraph(value = "Study.withZonesTags", type = EntityGraph.EntityGraphType.FETCH)
    List<Study> findFirst5ByMembersContainingAndClosedOrderByPublishedDateTimeDesc(Account account, boolean closed);

}
