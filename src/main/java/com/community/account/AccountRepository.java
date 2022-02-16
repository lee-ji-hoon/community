package com.community.account;

import com.community.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByStudentId(String studentId);

    Account findByEmail(String email);

    Account findByNickname(String emailOrNickname);

    Account findByUsername(String username);
}
