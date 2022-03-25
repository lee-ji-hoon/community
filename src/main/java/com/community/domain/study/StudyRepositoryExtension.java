package com.community.domain.study;

import com.community.domain.tag.Tag;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {

    List<Study> findByAccount(Set<Tag> tags);
}
