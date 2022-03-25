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
        log.info("fromAccountId : {}", account.getId());
        List<Alarm> alarmList = alarmRepository.deleteByToAccountAndChecked(account, true);
        account.getAlarmList().removeAll(alarmList);
        List<Alarm> accountAlarmList= account.getAlarmList();

        for (Alarm alarm : accountAlarmList) {
            log.info("삭제 후 accountAlarmList {}", alarm.getAlarmId());
            log.info("삭제 후 accountAlarmList {}", alarm.isChecked());
        }
    }

    public void checkedAll(Account account) {
        List<Alarm> notCheckedAlarmList = alarmRepository.findByToAccountAndChecked(account, false);
        for (Alarm alarm : notCheckedAlarmList) {
            readAlarm(alarm, account);
        }
        for (Alarm alarm : account.getAlarmList()) {
            log.info("체크 후 accountAlarmList {}", alarm.getAlarmId());
            log.info("체크 후 accountAlarmList {}", alarm.isChecked());
        }
    }

    private void readAlarm(Alarm alarm, Account account) {
        alarm.setChecked(true);
        for (Alarm accountAlarm : account.getAlarmList()) {
            if(Objects.equals(accountAlarm.getAlarmId(), alarm.getAlarmId())) {
                log.info("알람 체크 확인 account alarmList : {}, alarmList : {}",
                        accountAlarm.getAlarmId(), alarm.getAlarmId());
                accountAlarm.setChecked(true);
            }
        }
        account.deleteAlarmSize();

        alarmRepository.save(alarm);
    }
}
