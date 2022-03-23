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

    public void alarmRead(Alarm byId) {
        byId.setChecked(true);
        alarmRepository.save(byId);
    }

    @Transactional
    public void deleteByChecked(Account account) {
        log.info("fromAccountId : {}", account.getId());
        List<Alarm> alarmList = alarmRepository.deleteByToAccountAndChecked(account, true);
        for (Alarm alarm : alarmList) log.info("삭제 할 alarmList : {}", alarm);
        /*List<Alarm> AccountAlarmList = account.getAlarmList();
        for (Alarm alarm : AccountAlarmList) log.info("account alarmList : {}", alarm);*/

    }
}
