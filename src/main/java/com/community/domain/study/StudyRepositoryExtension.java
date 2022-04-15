package com.community.domain.study;

import com.community.domain.account.Account;
import com.community.domain.tag.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {

    List<Study> findByAccount(Set<Tag> tags);

    Page<Study> findByMembersContaining(Account account, Pageable pageable);

    Page<Study> findByManagersContaining(Account account, Pageable pageable);

}
