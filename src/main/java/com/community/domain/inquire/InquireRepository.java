package com.community.domain.inquire;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquireRepository extends JpaRepository<Inquire, Long> {

    List<Inquire> findByIsAnsweredOrderByUploadTimeDesc(Boolean isAnswered);

}
