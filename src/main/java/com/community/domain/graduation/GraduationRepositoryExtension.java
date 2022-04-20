package com.community.domain.graduation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GraduationRepositoryExtension {

    Page<Graduation> findByGraduationDate(int date, Pageable pageable);

    Page<Graduation> findAllGraduation(Pageable pageable);
}
