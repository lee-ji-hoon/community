package com.community.account.repository;

import com.community.account.entity.PersistentLogins;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistentLoginsRepository extends JpaRepository<PersistentLogins, Long> {

    void deleteByUsername(String nickname);
}
