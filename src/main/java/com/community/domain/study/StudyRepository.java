package com.community.domain.study;

import com.community.domain.account.Account;
import com.community.domain.study.Study;
import com.community.domain.tag.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long> {

    boolean existsByPath(String path);

    @EntityGraph(value = "Study.withAll", type = EntityGraph.EntityGraphType.LOAD)
    Study findByPath(String path);

    @EntityGraph(value = "Study.withTagsAndManagers", type = EntityGraph.EntityGraphType.FETCH)
    Study findAccountWithTagsByPath(String path);

    @EntityGraph(value = "Study.withManagers", type = EntityGraph.EntityGraphType.FETCH)
    Study findAccountWithManagersByPath(String path);

    @EntityGraph(value = "Study.withMembers", type = EntityGraph.EntityGraphType.FETCH)
    Study findStudyWithMembersByPath(String path);

    @EntityGraph(value = "Study.withAll", type = EntityGraph.EntityGraphType.FETCH)
    List<Study> findFirst9ByOrderByPublishedDateTimeDesc();

    @EntityGraph(value = "Study.withTagsAndManagers", type = EntityGraph.EntityGraphType.FETCH)
    List<Study> findByManagersContainingOrderByPublishedDateTimeDesc(Account account);

    @EntityGraph(value = "Study.withTagsAndMembers", type = EntityGraph.EntityGraphType.FETCH)
    List<Study> findByMembersContainingOrderByPublishedDateTimeDesc(Account account);

    @EntityGraph(value = "Study.withTagsAndMembers", type = EntityGraph.EntityGraphType.FETCH)
    List<Study> findByTagsContainingOrderByPublishedDateTimeDesc(Tag tag);

    @EntityGraph(value = "Study.withTagsAndMembers", type = EntityGraph.EntityGraphType.FETCH)
    List<Study> findFirst9ByOrderByMemberCount();

    @EntityGraph(value = "Study.withTags", type = EntityGraph.EntityGraphType.FETCH)
    Study findStudyWithTagsById(Long id);

    /* 검색 쿼리 */
    List<Study> findByTitleContaining(String keyword);

    List<Study> findByFullDescriptionContaining(String keyword);

    List<Study> findByTagsContaining(Tag tag);

}