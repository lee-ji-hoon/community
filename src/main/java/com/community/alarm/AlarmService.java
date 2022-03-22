package com.community.alarm;

import com.community.account.entity.Account;
import com.community.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public void alarmRead(Alarm byId) {
        byId.setChecked(true);
        alarmRepository.save(byId);
    }

    public void deleteByChecked(Account account) {
        log.info("fromAccountId : {}", account.getId());
        alarmRepository.deleteByToAccountAndChecked(account, true);
    }
}
