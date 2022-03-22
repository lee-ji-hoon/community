package com.community.alarm;

import com.community.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    long countByToAccountAndChecked(Account account, boolean checked);

    Alarm findByAlarmId(Long alarm);

    @Transactional
    List<Alarm> findByToAccountAndCheckedOrderByCreateAlarmTimeDesc(Account account, boolean checked);

    @Transactional
    void deleteByToAccountAndChecked(Account account, boolean checked);
}
