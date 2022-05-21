package com.community.domain.inquire;

import com.community.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InquireAnswerRepository extends JpaRepository<InquireAnswer, Long> {
    Optional<InquireAnswer> findByInquire(Inquire inquire);
}
