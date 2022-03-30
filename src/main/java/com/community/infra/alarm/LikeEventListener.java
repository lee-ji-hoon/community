package com.community.infra.alarm;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.alarm.Alarm;
import com.community.domain.alarm.AlarmRepository;
import com.community.domain.alarm.AlarmType;
import com.community.domain.board.Board;
import com.community.domain.likes.Likes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LikeEventListener {

    private final AlarmRepository alarmRepository;
    private final AccountRepository accountRepository;

    @EventListener
    public void likesCreate(LikeCreatePublish likeCreatePublish) {
        log.info("좋아요 실행");

        Likes likes = likeCreatePublish.getLikes();
        Account fromAccount = likeCreatePublish.getFromAccount();

        Board board = likes.getBoard();
        log.info("board : {}", board.getBid());

        Optional<Account> byId = accountRepository.findById(board.getWriter().getId());
        String nickname = byId.get().getNickname();

        log.info("writer : {}", nickname);
        if (byId.get().isLikesByPost()) {
            log.info("좋아요 발송 board : {}", board.getBid());
            log.info("좋아요 발송 account : {}", nickname);
            sendWebByBoardLikes(likes, board, byId, fromAccount);
        }

    }

    private void sendWebByBoardLikes(Likes likes, Board board, Optional<Account> byId, Account fromAccount) {
        Alarm alarm = new Alarm();
        alarm.setTitle(board.getTitle());
        alarm.setLink("/board/detail/"+board.getBid());
        alarm.setChecked(false);
        alarm.setCreateAlarmTime(LocalDateTime.now());
        alarm.setPath(String.valueOf(board.getBid()));
        alarm.setMessage(fromAccount.getNickname() + "님이 좋아요 누르셨습니다.");
        alarm.setToAccount(byId.get());
        alarm.setFromAccount(fromAccount);
        alarm.setAlarmType(AlarmType.LIKES);
        alarmRepository.save(alarm);
    }
}
