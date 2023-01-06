package com.community.app.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class Ut {
    public static String setDateTime(LocalDateTime localDateTime){
        final int SEC = 60;
        final int MIN = 60;
        final int HOUR = 24;
        final int DAY = 30;
        final int MONTH = 12;
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < SEC) {
            // sec
            msg = diffTime + "초 전";
        } else if ((diffTime /= SEC) < MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= HOUR) < DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }
}
