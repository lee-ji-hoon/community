package com.community.domain.alarm;

import com.community.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    long countByToAccountAndChecked(Account account, boolean checked);

    Alarm findByAlarmId(Long alarm);

    @Transactional(readOnly = false)
    List<Alarm> findByToAccountAndCheckedOrderByCreateAlarmTimeDesc(Account account, boolean checked);

    @Transactional(readOnly = false)
    List<Alarm> deleteByToAccountAndChecked(Account account, boolean checked);
}
