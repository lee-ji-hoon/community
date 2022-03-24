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

    public void readAlarm(Alarm alarm, Account account) {
        alarm.setChecked(true);
        account.checkedAlarm(alarm);
        account.deleteAlarmSize();

        alarmRepository.save(alarm);
        accountRepository.save(account);
    }

    public void deleteByChecked(Account account) {
        log.info("fromAccountId : {}", account.getId());
        List<Alarm> alarmList = alarmRepository.deleteByToAccountAndChecked(account, true);
        account.deleteCheckedAlarms(alarmList);

    }
}
