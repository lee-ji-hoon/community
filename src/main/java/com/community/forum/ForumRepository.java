package com.community.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ForumRepository extends JpaRepository<Forum, Long> {
    List<Forum> findAllTop5ByIsReportedOrderByPostUploadTime(Boolean isTrue);

    List<Forum> findAllByIsReportedAndForumTitleOrderByPostUploadTimeDesc(Boolean isTrue, String forumTitle);
    Forum findByFid(long fid);
}
