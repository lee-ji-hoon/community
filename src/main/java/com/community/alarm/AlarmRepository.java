package com.community.alarm;

import com.community.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    long countByAccountAndChecked(Account account, boolean checked);

    @Transactional
    List<Alarm> findByAccountAndCheckedOrderByCreateAlarmTimeDesc(Account account, boolean checked);

    @Transactional
    void deleteByAccountAndChecked(Account account, boolean checked);
}
