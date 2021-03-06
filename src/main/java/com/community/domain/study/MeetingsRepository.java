package com.community.domain.study;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface MeetingsRepository extends JpaRepository<Meetings, Long> {

    List<Meetings> findFirst9ByOrderByUploadTimeDesc();

    List<Meetings> findAllByStudyOrderByUploadTimeDesc(Study study);

    Meetings findByMeetingsId(long id);
}
