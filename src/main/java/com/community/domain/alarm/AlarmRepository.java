package com.community.domain.alarm;

import com.community.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    long countByToAccountAndChecked(Account account, boolean checked);

    Alarm findByAlarmId(Long alarm);

    List<Alarm> findFirst3ByToAccountAndCheckedOrderByCreateAlarmTimeDesc(Account account, boolean checked);

    @Transactional
    void deleteByToAccountAndChecked(Account account, boolean checked);

    List<Alarm> findByToAccountAndChecked(Account account, boolean b);
}
