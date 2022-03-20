package com.community.alarm;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.study.StudyService;
import com.community.study.entity.Study;
import com.community.tag.Tag;
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
//        alarmService.markAsRead(alarmList);
        return "alarm/view";
    }

    @GetMapping("/alarm/{type}/{path}")
    public String moveAlarmLink(@CurrentUser Account account, @PathVariable String type, @PathVariable String path, Model model) {
        log.info("alarmByPath : {}", path);
        log.info("alarmByType : {}", type);

        if (type.equals("study")) {
            log.info("study 페이징 이동 : {}", path);
            Study bypath = studyService.getPath(path);
            model.addAttribute(account);
            model.addAttribute(bypath);
            return "study/study-view";
        }


        return "alarm/view";

    }

    void alarmType(Model model, List<Alarm> alarmList, long countChecked, long countNotChecked) {
        List<Alarm> newStudyAlarms = new ArrayList<>();
        List<Alarm> newMeetingAlarms = new ArrayList<>();

        for (var alarm : alarmList) {
            switch (alarm.getAlarmType()) {
                case STUDY:
                    newStudyAlarms.add(alarm);
                    break;
                case MEETING:
                    newMeetingAlarms.add(alarm);
                    break;
            }
        }

        model.addAttribute("countChecked", countChecked);
        model.addAttribute("countNotChecked", countNotChecked);
        model.addAttribute("alarmList", alarmList);

        model.addAttribute("newStudyAlarms", newStudyAlarms);
        model.addAttribute("newMeetingAlarms", newMeetingAlarms);
    }
}
