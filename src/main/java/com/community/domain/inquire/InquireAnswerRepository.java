package com.community.domain.inquire;

import com.community.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquireAnswerRepository extends JpaRepository<InquireAnswer, Long> {
}
