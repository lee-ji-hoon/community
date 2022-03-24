package com.community.web.controller;

import com.community.domain.account.CurrentUser;
import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.alarm.Alarm;
import com.community.domain.alarm.AlarmRepository;
import com.community.domain.alarm.AlarmType;
import com.community.domain.board.Board;
import com.community.domain.board.Reply;
import com.community.service.*;
import com.community.web.dto.BoardForm;
import com.community.web.dto.ReplyForm;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.ReplyRepository;
import com.community.domain.likes.LikeRepository;
import com.community.domain.likes.Likes;
import com.community.web.dto.BoardReportForm;
import com.community.domain.report.BoardReportRepository;
import com.community.domain.report.ReplyReportRepository;
import com.community.domain.study.Meetings;
import com.community.domain.study.Study;
import com.community.web.dto.MeetingsForm;
import com.community.domain.study.MeetingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AlarmController {

    private final AlarmRepository alarmRepository;
    private final MeetingsRepository meetingsRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final AccountRepository accountRepository;
    private final BoardReportRepository boardReportRepository;
    private final ReplyReportRepository replyReportRepository;

    private final ReplyService replyService;
    private final LikeService likeService;
    private final StudyService studyService;
    private final AlarmService alarmService;
    private final BoardService boardService;

    @GetMapping("/alarm/view")
    public String alarmView(@CurrentUser Account account, Model model) {
        log.info("account.getNickname : {}", account.getNickname());
        List<Alarm> alarmList = alarmRepository.findByToAccountAndCheckedOrderByCreateAlarmTimeDesc(account, false);
        List<Alarm> byChecked = alarmRepository.findByToAccountAndCheckedOrderByCreateAlarmTimeDesc(account,true);

        for (Alarm alarm : alarmList) {
            log.info("alarm.account : {}", alarm.getToAccount().getNickname());
        }
        long countByAccountAndChecked = alarmRepository.countByToAccountAndChecked(account, true);
        alarmType(model, alarmList, countByAccountAndChecked, alarmList.size());

        model.addAttribute(account);
        model.addAttribute("byChecked", byChecked);
        return "alarm/view";
    }

    @ResponseBody
    @RequestMapping(value = "/alarm/deleteAll", method = RequestMethod.GET)
    public String deleteCheckedAlarm(@CurrentUser Account account){
        String message = null;
        log.info("삭제 할 알람 수 {}", alarmRepository.countByToAccountAndChecked(account, true));
        if(alarmRepository.countByToAccountAndChecked(account, true) == 0){
            message = "<div class=\"bg-red-500 border m-4 p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                    "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                    "        <i class=\"icon-feather-x\"></i>\n" +
                    "    </button>\n" +
                    "    <h3 class=\"text-lg font-semibold text-white\">오류</h3>\n" +
                    "    <p class=\"text-white text-opacity-75\">읽은 알람이 존재하지 않습니다.</p>\n" +
                    "</div>";
            return message;
        }
        alarmService.deleteByChecked(account);

        message = "<div class=\"bg-blue-500 border m-4 p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                "        <i class=\"icon-feather-x\"></i>\n" +
                "    </button>\n" +
                "    <h3 class=\"text-lg font-semibold text-white\">확인</h3>\n" +
                "    <p class=\"text-white text-opacity-75\">알림 삭제에 성공했습니다.</p>\n" +
                "</div>";
        return message;
    }

    @GetMapping("/alarm/{alarmId}")
    public String moveAlarmLink(@CurrentUser Account account,
                                @PathVariable Long alarmId, Model model,
                                HttpServletRequest request, HttpServletResponse response) {
        log.info("alarmByPath : {}", alarmId);
        log.info("accountId : {}", account.getId());

        Alarm byAlarmId = alarmRepository.findByAlarmId(alarmId);
        AlarmType type = byAlarmId.getAlarmType();
        String path = byAlarmId.getPath();

        alarmService.alarmRead(byAlarmId, account);

        log.info("byPathAndType : {}", byAlarmId);
        model.addAttribute(account);
        switch (type){
            case STUDY: log.info("study 페이지 이동 : {}", path);
                Study studyByPath = studyService.getPath(path);
                model.addAttribute(studyByPath);
                return "study/study-view";

            case MEETING: case MEETING_REPLY : log.info("meeting 페이지 이동 : {}", path);
                Study meetingByPath = studyService.getPath(path);
                List<Meetings> meetingsList = meetingsRepository.findAllByStudyOrderByUploadTimeDesc(meetingByPath);

                model.addAttribute("service", studyService);
                model.addAttribute("meetingsList", meetingsList);
                model.addAttribute(meetingByPath);
                model.addAttribute(new MeetingsForm());
                return "study/study-meetings";

            case BOARD_REPLY: case LIKES: log.info("board 페이지 이동 : {}", path);
                model.addAttribute("account", account);
                Long boardNumber = Long.valueOf(path);
                Boolean hasBoardError = boardService.boardReportedOrNull(boardNumber);
                if (hasBoardError) {
                    return "error-page";
                }

                boardService.viewUpdate(boardNumber, request, response);
                Board detail = boardRepository.findByBid(boardNumber);
                log.info("board detail : {}", path);

                // 최근에 올라온 게시물
                List<Board> recentlyBoards = boardRepository.findTop4ByIsReportedOrderByUploadTimeDesc(false);

                // 좋아요 및 댓글
                Optional<Likes> likes = likeRepository.findByAccountAndBoard(account, detail);
                List<Reply> replies = replyRepository.findAllByBoardOrderByUploadTimeDesc(detail);

                // 게시물 작성자 account 불러오는 로직
                Board currentBoard = boardRepository.findByBid(boardNumber);
                Account boardOwner = currentBoard.getWriter();

                model.addAttribute("board", detail);
                model.addAttribute("boardOwner", boardOwner);
                model.addAttribute("service", boardService);
                model.addAttribute("accountRepo", accountRepository);
                model.addAttribute("likes", likes);
                model.addAttribute("likeService", likeService);
                model.addAttribute("reply", replies);
                model.addAttribute("replyService", replyService);
                model.addAttribute("boardReport", boardReportRepository.existsByAccountAndBoard(account, detail));
                model.addAttribute("replyRepo", replyReportRepository);
                model.addAttribute("recentlyBoards", recentlyBoards);

                model.addAttribute(new ReplyForm());
                model.addAttribute(new BoardReportForm());
                model.addAttribute(new BoardForm());

                return "board/board-detail";
        }
        return "alarm/view";
    }

    void alarmType(Model model, List<Alarm> alarmList, long countChecked, long countNotChecked) {

        List<Alarm> newStudyAlarms = new ArrayList<>();
        List<Alarm> newMeetingAlarms = new ArrayList<>();
        List<Alarm> newMeetingsReplyAlarms = new ArrayList<>();
        List<Alarm> newBoardReplyAlarms = new ArrayList<>();
        List<Alarm> newLikesAlarms = new ArrayList<>();


        for (var alarm : alarmList) {
            switch (alarm.getAlarmType()) {
                case STUDY:
                    newStudyAlarms.add(alarm); break;
                case MEETING:
                    newMeetingAlarms.add(alarm); break;
                case MEETING_REPLY:
                    newMeetingsReplyAlarms.add(alarm); break;
                case BOARD_REPLY:
                    newBoardReplyAlarms.add(alarm); break;
                case LIKES:
                    newLikesAlarms.add(alarm); break;
            }
        }

        model.addAttribute("countChecked", countChecked);
        model.addAttribute("countNotChecked", countNotChecked);
        model.addAttribute("alarmList", alarmList);

        model.addAttribute("newStudyAlarms", newStudyAlarms);
        model.addAttribute("newMeetingAlarms", newMeetingAlarms);
        model.addAttribute("newMeetingsReplyAlarms", newMeetingsReplyAlarms);
        model.addAttribute("newBoardReplyAlarms", newBoardReplyAlarms);
        model.addAttribute("newLikesAlarms", newLikesAlarms);
    }
}
