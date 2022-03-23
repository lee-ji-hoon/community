package com.community.web.controller;

import com.community.domain.account.CurrentUser;
import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.infra.alarm.MeetingCreatedPublish;
import com.community.infra.alarm.ReplyCreatePublish;
import com.community.infra.alarm.StudyCreatedPublish;
import com.community.domain.board.Reply;
import com.community.web.dto.ReplyForm;
import com.community.domain.board.ReplyRepository;
import com.community.service.ReplyService;
import com.community.service.StudyService;
import com.community.domain.study.Meetings;
import com.community.domain.study.Study;
import com.community.web.dto.MeetingsForm;
import com.community.web.dto.StudyCalendarForm;
import com.community.domain.study.MeetingsRepository;
import com.community.domain.study.StudyRepository;
import com.community.web.dto.validator.MeetingFormValidator;
import com.community.web.dto.validator.StudyCalendarFormValidator;
import com.community.domain.tag.Tag;
import com.community.web.dto.TagForm;
import com.community.domain.tag.TagRepository;
import com.community.web.dto.StudyDescriptionForm;
import com.community.web.dto.StudyForm;
import com.community.web.dto.validator.StudyFormValidator;
import com.community.service.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StudyController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyFormValidator studyFormValidator;
    private final StudyCalendarFormValidator studyCalendarFormValidator;
    private final MeetingFormValidator meetingFormValidator;
    private final ObjectMapper objectMapper;
    private final TagService tagService;
    private final ReplyService replyService;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final TagRepository tagRepository;
    private final StudyRepository studyRepository;
    private final AccountRepository accountRepository;
    private final MeetingsRepository meetingsRepository;
    private final ReplyRepository replyRepository;

    private static final String STUDY_FORM_URL = "/study-form";
    private static final String STUDY_FORM_VIEW = "study/study-form";

    private static final String STUDY_PATH_URL = "/study/{path}";
    private static final String STUDY_PATH_VIEW = "study/{path}";

    private static final String STUDY_SETTINGS = STUDY_PATH_URL + "/settings/";

    private String fixPath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

    @InitBinder("studyForm")
    public void studyFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studyFormValidator);
    }

    @InitBinder("studyCalendarForm")
    public void studyDescriptionFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studyCalendarFormValidator);
    }

    @InitBinder("meetingsForm")
    public void meetingFormInitBinder(WebDataBinder webDataBinder) {
        log.info("webDataBinder={}, target={}", webDataBinder, webDataBinder.getTarget());
        webDataBinder.addValidators(meetingFormValidator);
    }

    @GetMapping("/study")
    public String study(@CurrentUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        model.addAttribute("studyListId", studyRepository.findFirst9ByOrderByPublishedDateTimeDesc());
        model.addAttribute("popularityStudyLIst", studyRepository.findFirst9ByOrderByMemberCount());
        model.addAttribute("enrolledStudyList", studyRepository.findByMembersContainingOrderByPublishedDateTimeDesc(account));
        model.addAttribute("myStudyList", studyRepository.findByManagersContainingOrderByPublishedDateTimeDesc(account));
        model.addAttribute("studyTagListTitle",tagRepository.findAll());

        model.addAttribute("accountWithTagsById", accountRepository.findAccountWithTagsById(account.getId()));

        return "study/study-list";
    }

    @GetMapping("/study/search/{tagTitle}")
    public String viewStudyWithTagTitle(@CurrentUser Account account, @PathVariable String tagTitle, Model model) {

        Tag byTag = tagService.getTag(tagTitle);

        model.addAttribute(account);
        model.addAttribute("tag", byTag);
        model.addAttribute("accountWithTagsById", accountRepository.findAccountWithTagsById(account.getId()));
        model.addAttribute("studyListWithTag", studyRepository.findByTagsContainingOrderByPublishedDateTimeDesc(byTag));
        model.addAttribute("studyTagListTitle",tagRepository.findAll());

        return "study/study-with-tag";
    }

    // 스터디 추가 뷰
    @GetMapping(STUDY_FORM_URL)
    public String newStudyForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new StudyForm());
        return STUDY_FORM_VIEW;

    }

    // 스터디 추가
    @PostMapping(STUDY_FORM_URL)
    public String newStudySubmit(@CurrentUser Account account, @Valid StudyForm studyForm, Errors errors, Model model,
                                 HttpServletRequest httpServletRequest) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return STUDY_FORM_VIEW;
        }

        Study newStudy = studyService.createNewStudy(modelMapper.map(studyForm, Study.class), account);

        return "redirect:/study/" + URLEncoder.encode(newStudy.getPath(), StandardCharsets.UTF_8);
    }
    // 스터디 뷰 이동

    // 모임 페이지
    @GetMapping(STUDY_PATH_URL + "/meetings")
    public String meetingListView(@CurrentUser Account account,
                                  @PathVariable String path, Model model) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        List<Meetings> meetingsList = meetingsRepository.findAllByStudyOrderByUploadTimeDesc(studyUpdate);

        model.addAttribute("meetingsList", meetingsList);
        model.addAttribute("service", studyService);
        model.addAttribute(account);
        model.addAttribute(studyUpdate);
        model.addAttribute(new MeetingsForm());
        return "study/study-meetings";
    }

    @PostMapping(STUDY_PATH_URL + "/meetings")
    public String meetingList(@CurrentUser Account account, @PathVariable String path,
                              Model model, @Valid MeetingsForm meetingsForm) {



        log.info("모임 생성");
        Study studyUpdate = studyService.getStudyToUpdateStatus(account, path);
/*        if (errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(studyUpdate);
            return "study/meetings/view";
        }*/
        Meetings newMeeting = studyService.createNewMeeting(modelMapper.map(meetingsForm, Meetings.class), studyUpdate, account);
        applicationEventPublisher.publishEvent(new MeetingCreatedPublish(newMeeting, account));
        model.addAttribute(account);

        log.info("모임 생성 성공");
        return "redirect:/study/" + URLEncoder.encode(studyUpdate.getPath(), StandardCharsets.UTF_8) + "/meetings";
    }

    @GetMapping(STUDY_PATH_URL + "/meetings/{meetingsId}")
    public String deleteMeeting(@CurrentUser Account account, @PathVariable String path,
                                Model model, @PathVariable long meetingsId) {
        log.info("모임 삭제 실행");
        Study studyUpdate = studyService.getStudyToUpdateStatus(account, path);

        meetingsRepository.deleteById(meetingsId);

        model.addAttribute(account);

        log.info("모임 삭제 성공");

        return "redirect:/study/" + URLEncoder.encode(studyUpdate.getPath(), StandardCharsets.UTF_8) + "/meetings";
    }

    @ResponseBody
    @RequestMapping(value = "/study/meetings/update", method = RequestMethod.GET)
    public String meetingUpdate(MeetingsForm meetingsForm, @CurrentUser Account account,
                                @RequestParam(required = false, value = "meetingId") String meetingId,
                                @RequestParam(required = false, value = "updateTitle") String updateTitle,
                                @RequestParam(required = false, value = "updateMethod") String updateMethod,
                                @RequestParam(required = false, value = "updateMeetingDescription") String updateMeetingDescription,
                                @RequestParam(required = false, value = "updatePlaces") String updatePlaces){

        log.info("모임 아이디 : ", meetingId);
        log.info("모임 제목 : ", updateTitle);
        log.info("모임 주제 : ", updateMethod);
        log.info("모임 내용 : ", updateMeetingDescription);
        log.info("모임 위치 : ", updatePlaces);


        Long meetingsId = Long.valueOf(meetingId);
        Meetings meetings = meetingsRepository.findByMeetingsId(meetingsId);
        String message = null;

        if (account.getId().equals(meetings.getWriter().getId())) {
            meetingsForm.setMeetingTitle(updateTitle);
            meetingsForm.setMeetingMethod(updateMethod);
            meetingsForm.setMeetingDescription(updateMeetingDescription);
            meetingsForm.setMeetingPlaces(updatePlaces);
            studyService.updateMeeting(meetingsId, meetingsForm);
            message = "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                    "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                    "        <i class=\"icon-feather-x\"></i>\n" +
                    "    </button>\n" +
                    "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                    "    <p class=\"text-white text-opacity-75\">게시물이 수정되었습니다.</p>\n" +
                    "</div>";
            return message;
        }
        log.info("잘못된 게시물 수정 요청 : bid = " + meetingId + " accountId = " + account.getId());
        message = "잘못된 요청입니다.";
        return message;
    }

    @ResponseBody
    @RequestMapping(value = "/study/meetings/reply")
    public int addMeetingReplyLink(@RequestParam(value = "r_meetings_id") Long r_meetings_id,
                                   @RequestParam(value = "r_account_id") Long r_account_id,
                                   @RequestParam(value = "r_content") String r_content,
                                   ReplyForm replyForm) throws IOException {
        log.info("댓글 작성 호출");
        log.info(r_meetings_id + "r_meetings_id");
        log.info(r_account_id + "r_account_id");
        log.info(r_content + "r_content");

        replyForm.setContent(r_content);


        Optional<Account> currentAccount = accountRepository.findById(r_account_id);

        if (currentAccount.isPresent()) {
            String accountEmail = currentAccount.get().getEmail();
            Account account = accountRepository.findByEmail(accountEmail);
            Meetings currentMeetings = meetingsRepository.findByMeetingsId(r_meetings_id);
            Reply reply = replyService.saveMeetingsReply(replyForm, account, currentMeetings);

            Set<Account> managers = currentMeetings.getStudy().getManagers();
            for (Account manager : managers) {
                Optional<Account> managerId = accountRepository.findById(manager.getId());
                log.info("manager : {}",managerId.get().getId());
                log.info("account : {}",currentAccount.get().getId());
                if(!managerId.get().getId().equals(currentAccount.get().getId())) {
                    applicationEventPublisher.publishEvent(new ReplyCreatePublish(reply, account));
                }
            }

            List<Reply> replies = replyRepository.findAll();
            int reply_size = replies.size();
            return reply_size;
        }
        List<Reply> replies = replyRepository.findAll();
        int reply_size = replies.size();
        return reply_size;
    }

    @ResponseBody
    @RequestMapping(value = "/study/meetings/reply/update")
    public int addMeetingReplyUpdate(@RequestParam(value = "reply_update_rid") Long reply_update_rid,
                                     @RequestParam(value = "reply_update_content") String reply_update_content) throws IOException{
        log.info("rid : " + reply_update_rid);
        log.info("content : " + reply_update_content);

        replyService.updateReply(reply_update_rid, reply_update_content);

        int reply_size = 0;
        return reply_size;
    }

    @ResponseBody
    @RequestMapping(value = "/study/meetings/reply/delete")
    public int replyDelete(@RequestParam(value = "reply_delete_rid") Long reply_delete_rid) throws IOException{
        log.info("rid : " + reply_delete_rid);
        Reply byRid = replyRepository.findByRid(reply_delete_rid);
        log.info("delete_reply" + byRid);
        replyRepository.delete(byRid);

        int reply_size = 0;
        return reply_size;
    }
    // 모임 댓글 추가 끝

    @GetMapping(STUDY_PATH_URL)
    public String viewStudy(@CurrentUser Account account, @PathVariable String path, Model model) {

        Study bypath = studyService.getPath(path);

        model.addAttribute(account);
        model.addAttribute(bypath);

        return "study/study-view";
    }

    // 스터디 참여
    @GetMapping(STUDY_PATH_VIEW + "/join")
    public String joinStudy(@CurrentUser Account account, @PathVariable String path) {
        Study studyWithMembersByPath = studyRepository.findStudyWithMembersByPath(path);

        studyService.addMember(studyWithMembersByPath, account);

        return "redirect:/study/" + fixPath(path) + "/meetings";
    }

    // 스터디 탈퇴
    @GetMapping(STUDY_PATH_VIEW + "/remove")
    public String removeStudy(@CurrentUser Account account, @PathVariable String path) {
        Study studyWithMembersByPath = studyRepository.findStudyWithMembersByPath(path);
        studyService.removeMember(studyWithMembersByPath, account);

        return "redirect:/study/" + fixPath(path);
    }

    // 스터디 설명 수정 시작
    @GetMapping(STUDY_SETTINGS + "description")
    public String studyDescriptionForm(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        model.addAttribute(account);
        model.addAttribute(studyUpdate);
        model.addAttribute(modelMapper.map(studyUpdate, StudyDescriptionForm.class));
        return "study/settings/description";
    }


    @PostMapping(STUDY_SETTINGS + "description")
    public String updateStudyDescriptionForm(@CurrentUser Account account, @PathVariable String path, String image,
                                  @Valid StudyDescriptionForm studyDescriptionForm,
                                  Errors errors, Model model, RedirectAttributes redirectAttributes) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        if (errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(studyUpdate);
            return "study/settings/description";
        }

        studyService.updateStudyDescription(studyUpdate, studyDescriptionForm);
        redirectAttributes.addFlashAttribute("message", "게시글의 소개가 수정됐습니다.");

        return "redirect:/study/" + fixPath(path) + "/settings/description";
    }
    // 스터디 설명 수정 끝

    @GetMapping(STUDY_SETTINGS + "calendar")
    public String studyCalendarForm(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        model.addAttribute(account);
        model.addAttribute(studyUpdate);
        model.addAttribute(modelMapper.map(studyUpdate, StudyCalendarForm.class));

        return "study/settings/calendar";
    }

    @PostMapping(STUDY_SETTINGS + "calendar")
    public String updateStudyDescriptionForm(@CurrentUser Account account, @PathVariable String path, String image,
                                             @Valid StudyCalendarForm studyCalendarForm,
                                             Errors errors, Model model, RedirectAttributes redirectAttributes) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        if (errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(studyUpdate);
            return "study/settings/calendar";
        }

        studyService.updateStudyCalendar(studyUpdate, studyCalendarForm);
        redirectAttributes.addFlashAttribute("message", "게시글의 일정이 수정됐습니다.");

        return "redirect:/study/" + fixPath(path) + "/settings/calendar";
    }

    @GetMapping(STUDY_SETTINGS + "alarm")
    public String sendStudyAlarmView(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        model.addAttribute(account);
        model.addAttribute(studyUpdate);

        return "study/settings/alarm";
    }

    @PostMapping(STUDY_SETTINGS + "alarm")
    public String sendStudyAlarm(@CurrentUser Account account, @PathVariable String path,
                                 RedirectAttributes redirectAttributes, Model model) {
        log.info("알람 실행");
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        boolean checkAlarmDateTime = studyService.checkAlarmDateTime(studyUpdate);

        log.info("study tags getTags: {}", studyUpdate.getTags());

        // 스터디 태그 미설정 시
        if (studyUpdate.getTags().isEmpty()) {
            log.info("study tags 비어있음 getTags: {}", studyUpdate.getTags());
            model.addAttribute(account);
            redirectAttributes.addFlashAttribute("alert", "스터디 주제 설정 후 알림을 보내주시기 바랍니다.");
            return "redirect:/study/" + fixPath(path) + "/settings/alarm";
        }

        /*// 24시간 체크 로직
        if(!checkAlarmDateTime){
            log.info("study 알림 최근 시간 :{} 현재 시간 :{} ", studyUpdate.getRecentAlarmDateTime(), LocalDateTime.now());
            redirectAttributes.addFlashAttribute("alert", "스터디 알람은 24시간에 한 번씩만 가능합니다.");
            return "redirect:/study/" + fixPath(path) + "/settings/alarm";
        }*/

        applicationEventPublisher.publishEvent(new StudyCreatedPublish(studyUpdate, account));

        redirectAttributes.addFlashAttribute("message", "관심분야로 설정된 회원들에게 알림을 전송했습니다.");

        return "redirect:/study/" + fixPath(path) + "/settings/alarm";
    }

    @PostMapping(STUDY_SETTINGS + "banner")
    public String studyImageUpToDate(@CurrentUser Account account, @PathVariable String path, String image, RedirectAttributes redirectAttributes) {

        Study studyUpdate = studyService.getStudyUpdate(account, path);
        studyService.getStudyImage(studyUpdate, image);

        redirectAttributes.addFlashAttribute("message", "이미지가 수정됐습니다.");

        return "redirect:/study/" + fixPath(path) ;
    }

    // 태그 수정 시작
    @GetMapping(STUDY_SETTINGS + "tags")
    public String studyTagsForm(@CurrentUser Account account, @PathVariable String path, Model model) throws JsonProcessingException {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(studyUpdate);

        model.addAttribute("tags", studyUpdate.getTags().stream().map(Tag::getTitle).collect(Collectors.toList()));
        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("tagList", objectMapper.writeValueAsString(allTags));

        return "study/settings/tags";
    }

    @PostMapping(STUDY_SETTINGS + "tags/add")
    @ResponseBody
    public ResponseEntity studyTagsAdd(@CurrentUser Account account, @PathVariable String path, @RequestBody TagForm tagForm) {

        Study studyUpdateTag = studyService.getStudyUpdateTag(account, path);
        Tag byTitle = tagService.findOrAdd(tagForm.getTagTitle());

        studyService.addTag(studyUpdateTag, byTitle);
        System.out.println("byTitle = " + byTitle);
        System.out.println("studyUpdateTag = " + studyUpdateTag);
        return ResponseEntity.ok().build();
    }

    @PostMapping(STUDY_SETTINGS + "tags/remove")
    @ResponseBody
    public ResponseEntity studyTagsRemove(@CurrentUser Account account, @PathVariable String path, @RequestBody TagForm tagForm) {

        Study studyUpdateTag = studyService.getStudyUpdateTag(account, path);
        Tag byTitle = tagRepository.findByTitle(tagForm.getTagTitle());

        if (byTitle == null) {
            return ResponseEntity.badRequest().build();
        }

        studyService.removeTag(studyUpdateTag, byTitle);

        return ResponseEntity.ok().build();
    }
    // 태그 수정 끝

    /* 스터디 전체적인 설정 시작 */
    // 스터디 설정 시작
    @GetMapping(STUDY_SETTINGS + "config")
    public String studyForm(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        model.addAttribute(account);
        model.addAttribute(studyUpdate);

        return "study/settings/config";
    }

    // 스터디 이름 변경
    @PostMapping(STUDY_SETTINGS + "config/title")
    public String studyTitleUpdate(@CurrentUser Account account, @PathVariable String path, RedirectAttributes redirectAttributes, Model model, String newTitle) {
        Study studyToUpdateStatus = studyService.getStudyToUpdateStatus(account, path);
        if (!studyService.isValidTitle(newTitle)) {
            model.addAttribute(account);
            model.addAttribute(studyToUpdateStatus);
            model.addAttribute("studyTitleError", "스터디 이름들 다시 입력해주세요");
            return "study/settings/config";
        }

        studyService.updateStudyTitle(studyToUpdateStatus, newTitle);
        redirectAttributes.addFlashAttribute("message", "스터디 이름을 수정했습니다.");

        return "redirect:/study/" + fixPath(path) + "/settings/config";
    }

    // 스터디 삭제
    @PostMapping(STUDY_SETTINGS + "config/remove")
    public String removeStudy(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        studyService.remove(study);
        return "redirect:/";
    }

    // 스터디 경로 수정
    @PostMapping(STUDY_SETTINGS + "config/path")
    public String updatePath(@CurrentUser Account account, @PathVariable String path, String newPath, Model model, RedirectAttributes redirectAttributes) {
        Study studyToUpdateStatus = studyService.getStudyToUpdateStatus(account, path);
        if (!studyService.isValidPath(newPath)) {
            model.addAttribute(account);
            model.addAttribute(studyToUpdateStatus);
            model.addAttribute("studyPathError", "해당 경로는 변경이 불가능합니다. 다른 값을 입력해주세요");
            return "study/settings/study";
        }
        studyService.updateStudyPath(studyToUpdateStatus, newPath);
        redirectAttributes.addFlashAttribute("message", "스터디 경로가 수정됐습니다.");
        return "redirect:/study/" + fixPath(newPath) + "/settings/config";

    }
    /* 스터디 전체적인 설정 끝 */


}
