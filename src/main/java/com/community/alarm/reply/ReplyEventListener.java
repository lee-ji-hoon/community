package com.community.alarm.reply;


import com.community.account.entity.Account;
import com.community.alarm.Alarm;
import com.community.alarm.AlarmRepository;
import com.community.alarm.AlarmType;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.study.entity.Meetings;
import com.community.study.entity.Study;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@Async
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReplyEventListener {

    private final AlarmRepository alarmRepository;

    @EventListener
    public void replyCreate(ReplyCreatePublish replyCreatePublish) {
        log.info("댓글 실행");

        Reply reply = replyCreatePublish.getReply();
        Account fromAccount = replyCreatePublish.getFromAccount();

        if (reply.getMeetings() != null) {
            Meetings meetings = reply.getMeetings();
            Study study = meetings.getStudy();
            log.info("meetings : {}", meetings);

            Set<Account> managers = study.getManagers();

            for (Account manager : managers) {
                log.info("manager : {}", manager.getNickname());
                if (manager.isReplyByMeetings()) {
                    log.info("모임 댓글 발송 study : {}", meetings.getMeetingsId() );
                    log.info("모임 댓글 발송 account : {}", manager.getNickname());
                    sendWebByMeetingsReply(reply, meetings, manager, study, fromAccount);
                }
            }
        } else if (reply.getBoard() != null) {
            Board board = reply.getBoard();
            log.info("board : {}", board);

            Account writer = board.getWriter();
            if (writer.isReplyByPost()) {
                log.info("댓글 발송 board : {}", board.getBid());
                log.info("댓글 발송 account : {}", writer.getNickname());
                sendWebByBoardReply(reply, board, writer, fromAccount);
            }
        }

    }

    private void sendWebByBoardReply(Reply reply, Board board, Account writer, Account fromAccount) {
        Alarm alarm = new Alarm();
        alarm.setTitle(board.getTitle());
        alarm.setLink("/board/detail/"+board.getBid());
        alarm.setChecked(false);
        alarm.setCreateAlarmTime(LocalDateTime.now());
        alarm.setPath(String.valueOf(board.getBid()));
        alarm.setMessage(reply.getContent());
        alarm.setToAccount(writer);
        alarm.setFromAccount(fromAccount);
        alarm.setAlarmType(AlarmType.BOARD_REPLY);
        alarmRepository.save(alarm);
    }

    private void sendWebByMeetingsReply(Reply reply, Meetings meetings, Account toAccount, Study study, Account fromAccount) {
        Alarm alarm = new Alarm();
        alarm.setTitle(meetings.getMeetingTitle());
        alarm.setLink("/study/" + study.getEncodePath() +"/meetings");
        alarm.setChecked(false);
        alarm.setCreateAlarmTime(LocalDateTime.now());
        alarm.setPath(study.getPath());
        alarm.setMessage(reply.getContent());
        alarm.setToAccount(toAccount);
        alarm.setFromAccount(fromAccount);
        alarm.setAlarmType(AlarmType.MEETING_REPLY);
        alarmRepository.save(alarm);
    }

}
