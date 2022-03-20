package com.community.alarm;

import com.community.account.entity.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Alarm {
    @Id
    @GeneratedValue
    @Column(name = "alarm_id ")
    private Long id;

    private String title;

    // 웹 by 웹 접근
    private String path;

    // 이메일 by 웹
    private String link;

    private String message;

    @ManyToOne
    private Account account;

    private LocalDateTime createAlarmTime;

    private boolean checked;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
}
