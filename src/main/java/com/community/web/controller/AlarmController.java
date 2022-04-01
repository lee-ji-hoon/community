package com.community.web.controller;

import com.community.domain.account.CurrentUser;
import com.community.domain.account.Account;
import com.community.domain.alarm.Alarm;
import com.community.domain.alarm.AlarmRepository;
import com.community.domain.alarm.AlarmType;
import com.community.service.*;
import com.community.domain.study.Study;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AlarmController {

    private final AlarmRepository alarmRepository;
    private final StudyService studyService;
    private final AlarmService alarmService;

    @GetMapping("/alarm/view")
    public String alarmView(@CurrentUser Account account, Model model) {
        List<Alarm> alarmList = alarmRepository.findByToAccountAndCheckedOrderByCreateAlarmTimeDesc(account, false);
        List<Alarm> byChecked = alarmRepository.findByToAccountAndCheckedOrderByCreateAlarmTimeDesc(account,true);

        long countByAccountAndChecked = alarmRepository.countByToAccountAndChecked(account, true);
        alarmType(model, alarmList, countByAccountAndChecked, alarmList.size());

        model.addAttribute(account);
        model.addAttribute("byChecked", byChecked);
        return "alarm/view";
    }


    @ResponseBody
    @RequestMapping(value = "/alarm/allChecked", method = RequestMethod.GET)
    public ResponseEntity checkedAllAlarm(@CurrentUser Account account) {
        log.info("알람 모두 읽기 실행");
        long count = alarmRepository.countByToAccountAndChecked(account, false);

        if(count > 0) {
            alarmService.checkedAll(account);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @ResponseBody
    @RequestMapping(value = "/alarm/deleteAll", method = RequestMethod.GET)
    public ResponseEntity deleteCheckedAlarm(@CurrentUser Account account){
        long accountAndChecked = alarmRepository.countByToAccountAndChecked(account, true);
        log.info("삭제 할 알람 수 {}", accountAndChecked);
        if(accountAndChecked == 0){
            return ResponseEntity.badRequest().build();
        }
        alarmService.deleteByChecked(account);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/alarm/detail/{alarmId}")
    public String moveAlarmLink(@CurrentUser Account account,
                                @PathVariable Long alarmId, Model model,
                                HttpServletRequest request, HttpServletResponse response) {

        Alarm byAlarmId = alarmRepository.findByAlarmId(alarmId);
        AlarmType type = byAlarmId.getAlarmType();
        String path = byAlarmId.getPath();

        alarmService.checked(byAlarmId, account);

        model.addAttribute(account);

        switch (type){
            case STUDY: log.info("study 페이지 이동 : {}", path);
                Study studyByPath = studyService.getPath(path);
                return "redirect:/study/" + URLEncoder.encode(studyByPath.getPath(), StandardCharsets.UTF_8);

            case MEETING: case MEETING_REPLY : log.info("meeting 페이지 이동 : {}", path);
                Study meetingByPath = studyService.getPath(path);
                return "redirect:/study/" + URLEncoder.encode(meetingByPath.getPath(), StandardCharsets.UTF_8) + "/meetings";

            case BOARD_REPLY: case LIKES: log.info("board 페이지 이동 : {}", path);
                Long boardNumber = Long.valueOf(path);
                return "redirect:/board/detail/"+boardNumber;
        }
        return "error-page";
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
