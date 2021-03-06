package com.community.domain.account;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long>, QuerydslPredicateExecutor<Account> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByStudentId(String studentId);

    Account findByEmail(String email);

    Account findByNickname(String emailOrNickname);

    @EntityGraph(value = "Account.withTags", type = EntityGraph.EntityGraphType.FETCH)
    Account findAccountWithTagsById(Long Id);

}
