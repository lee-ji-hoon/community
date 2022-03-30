package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.alarm.Alarm;
import com.community.domain.alarm.AlarmRepository;
import com.community.web.controller.BaseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
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

    public void checked(Alarm alarm, Account account) {
        log.info("checked 실행");
        readAlarm(alarm, account);
    }

    public void deleteByChecked(Account account) {
        alarmRepository.deleteByToAccountAndChecked(account, true);
    }

    public void checkedAll(Account account) {
        List<Alarm> notCheckedAlarmList = alarmRepository.findByToAccountAndChecked(account, false);
        for (Alarm alarm : notCheckedAlarmList) {
            readAlarm(alarm, account);
        }
    }

    private void readAlarm(Alarm alarm, Account account) {
        alarm.setChecked(true);
    }
}
