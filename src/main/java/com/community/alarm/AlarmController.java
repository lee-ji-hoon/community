package com.community.alarm;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.study.StudyService;
import com.community.study.entity.Meetings;
import com.community.study.entity.Study;
import com.community.study.form.MeetingsForm;
import com.community.study.repository.MeetingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AlarmController {

    private final AlarmRepository alarmRepository;
    private final MeetingsRepository meetingsRepository;

    private final StudyService studyService;
    private final AlarmService alarmService;

    @GetMapping("/alarm/view")
    public String alarmView(@CurrentUser Account account, Model model) {
        List<Alarm> alarmList = alarmRepository.findByAccountAndCheckedOrderByCreateAlarmTimeDesc(account, false);
        long countByAccountAndChecked = alarmRepository.countByAccountAndChecked(account, true);
        alarmType(model, alarmList, countByAccountAndChecked, alarmList.size());

        model.addAttribute(account);
        model.addAttribute("service", alarmService);
        model.addAttribute("new", true);
        return "alarm/view";
    }

    @GetMapping("/alarm/{alarmId}")
    public String moveAlarmLink(@CurrentUser Account account,
                                @PathVariable Long alarmId, Model model) {
        log.info("alarmByPath : {}", alarmId);
        log.info("accountId : {}", account.getId());

        Alarm byAlarmId = alarmRepository.findByAlarmId(alarmId);
        AlarmType type = byAlarmId.getAlarmType();
        String path = byAlarmId.getPath();

        alarmService.alarmRead(byAlarmId);

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
            /*case REPLY: log.info("reply 페이지 이동 : {}", path);
                Study meetingReplyPath = studyService.getPath(path);*/


        }
        return "alarm/view";
    }

    void alarmType(Model model, List<Alarm> alarmList, long countChecked, long countNotChecked) {
        List<Alarm> newStudyAlarms = new ArrayList<>();
        List<Alarm> newMeetingAlarms = new ArrayList<>();
        List<Alarm> newMeetingsReplyAlarms = new ArrayList<>();
        List<Alarm> byChecked = alarmRepository.findByChecked(true);

        for (var alarm : alarmList) {
            switch (alarm.getAlarmType()) {
                case STUDY:
                    newStudyAlarms.add(alarm);
                    break;
                case MEETING:
                    newMeetingAlarms.add(alarm);
                    break;
                case MEETING_REPLY:
                    newMeetingsReplyAlarms.add(alarm);
            }
        }

        model.addAttribute("countChecked", countChecked);
        model.addAttribute("countNotChecked", countNotChecked);
        model.addAttribute("alarmList", alarmList);

        model.addAttribute("newStudyAlarms", newStudyAlarms);
        model.addAttribute("newMeetingAlarms", newMeetingAlarms);
        model.addAttribute("newMeetingsReplyAlarms", newMeetingsReplyAlarms);
        model.addAttribute("byChecked", byChecked);
    }
}
