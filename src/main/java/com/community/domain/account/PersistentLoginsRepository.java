package com.community.domain.account;

import com.community.domain.account.PersistentLogins;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistentLoginsRepository extends JpaRepository<PersistentLogins, Long> {

    void deleteByUsername(String nickname);
}
