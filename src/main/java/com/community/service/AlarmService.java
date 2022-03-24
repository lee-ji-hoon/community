package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.alarm.Alarm;
import com.community.domain.alarm.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final AccountRepository accountRepository;

    public void alarmRead(Alarm alarm, Account account) {
        alarm.setChecked(true);
        account.checkedAlarm(alarm);
        alarmRepository.save(alarm);
    }

    public void deleteByChecked(Account account) {
        log.info("fromAccountId : {}", account.getId());
//        List<Alarm> byToAccountAndChecked = alarmRepository.findByToAccountAndChecked(account, true);
//        alarmRepository.deleteAll(byToAccountAndChecked);

        List<Alarm> alarmList = alarmRepository.deleteByToAccountAndChecked(account, true);
        for (Alarm alarm : alarmList) log.info("삭제 할 alarmList : {}", alarm.getAlarmId());

        account.deleteCheckedAlarms(alarmList);

        List<Alarm> accountAfterAccountAlarmList = account.getAlarmList();
        for (Alarm alarm : accountAfterAccountAlarmList) log.info("삭제 후 : account alarmList : {}", alarm.getAlarmId());

    }
}
