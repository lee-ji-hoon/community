package com.community.domain.inquire;

import com.community.domain.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquireRepository extends JpaRepository<Inquire, Long> {

    List<Inquire> findByIsAnsweredAndAccountOrderByUploadTimeDesc(Boolean isAnswered, Account account);

    Page<Inquire> findByIsAnsweredOrderByUploadTimeDesc(Boolean isAnswered, Pageable pageable);

}
