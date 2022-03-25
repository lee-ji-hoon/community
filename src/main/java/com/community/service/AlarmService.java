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
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final AccountRepository accountRepository;

    public void checked(Alarm alarm, Account account) {
        readAlarm(alarm, account);
    }

    public void deleteByChecked(Account account) {
        List<Alarm> alarmList = alarmRepository.deleteByToAccountAndChecked(account, true);
        account.getAlarmList().removeAll(alarmList);
    }

    public void checkedAll(Account account) {
        List<Alarm> notCheckedAlarmList = alarmRepository.findByToAccountAndChecked(account, false);
        for (Alarm alarm : notCheckedAlarmList) {
            readAlarm(alarm, account);
        }
    }

    private void readAlarm(Alarm alarm, Account account) {
        alarm.setChecked(true);
        for (Alarm accountAlarm : account.getAlarmList()) {
            if(Objects.equals(accountAlarm.getAlarmId(), alarm.getAlarmId())) {
                accountAlarm.setChecked(true);
            }
        }
        account.deleteAlarmSize();

        alarmRepository.save(alarm);
    }
}
