package com.community.study;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.study.form.StudyCalendarForm;
import com.community.tag.Tag;
import com.community.tag.TagForm;
import com.community.tag.TagRepository;
import com.community.study.form.StudyDescriptionForm;
import com.community.study.form.StudyForm;
import com.community.study.validator.StudyFormValidator;
import com.community.tag.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StudyController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyFormValidator studyFormValidator;
    private final ObjectMapper objectMapper;
    private final TagService tagService;

    private final TagRepository tagRepository;
    private final StudyRepository studyRepository;

    private static final String STUDY_FORM_URL = "/study-form";
    private static final String STUDY_FORM_VIEW = "study/form";

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
        webDataBinder.addValidators(studyFormValidator);
    }

    @GetMapping("/study")
    public String study(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute("studyListId", studyRepository.findFirst9ByOrderByPublishedDateTimeDesc());
        return "study/study-list";
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
    public String newStudySubmit(@CurrentUser Account account, @Valid StudyForm studyForm, Errors errors, Model model, HttpServletRequest httpServletRequest) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return STUDY_FORM_VIEW;
        }

        System.out.println("========================");
        System.out.println(studyForm.getStudyPlaces());
        System.out.println(studyForm.getPath());
        System.out.println("========================");

        String parameter = httpServletRequest.getParameter("startStudyDate");
        String parameter1 = httpServletRequest.getParameter("limitStudyDate");
        String parameter2 = httpServletRequest.getParameter("limitMemberDate");
        String studyPlaces = httpServletRequest.getParameter("studyPlaces");

        log.info("스티더 위치 = " + studyPlaces);
        log.info("스티더 시작일 = " + parameter);
        log.info("스티더 종료일 = " + parameter1);
        log.info("인원모집 종료일 = " + parameter2);

        Study newStudy = studyService.createNewStudy(modelMapper.map(studyForm, Study.class), account);
        return "redirect:/study/" + URLEncoder.encode(newStudy.getPath(), StandardCharsets.UTF_8);
    }

    // 스터디 뷰 이동
    @GetMapping(STUDY_PATH_URL)
    public String viewStudy(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study bypath = studyService.getPath(path);

        model.addAttribute(account);
        model.addAttribute(bypath);

        return "study/view";
    }

    // 멤버 확인
    @GetMapping(STUDY_PATH_URL + "/members")
    public String viewStudyMembers(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study bypath = studyService.getPath(path);

        model.addAttribute(account);
        model.addAttribute(bypath);

        return "study/members";
    }

    // 스터디 참여
    @GetMapping(STUDY_PATH_VIEW + "/join")
    public String joinStudy(@CurrentUser Account account, @PathVariable String path) {
        Study studyWithMembersByPath = studyRepository.findStudyWithMembersByPath(path);

        studyService.addMember(studyWithMembersByPath, account);

        return "redirect:/study/" + fixPath(path);
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

    @PostMapping(STUDY_SETTINGS + "banner")
    public String studyImageUpToDate(@CurrentUser Account account, @PathVariable String path, String image, RedirectAttributes redirectAttributes) {

        Study studyUpdate = studyService.getStudyUpdate(account, path);
        studyService.getStudyImage(studyUpdate, image);

        redirectAttributes.addFlashAttribute("message", "이미지가 수정됐습니다.");

        return "redirect:/study/" + fixPath(path) + "/settings/description";
    }

    @PostMapping(STUDY_SETTINGS + "banner/enable")
    public String studyBannerEnable(@CurrentUser Account account, @PathVariable String path) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        studyService.studyBannerEnable(studyUpdate);

        return "redirect:/study/" + fixPath(path) + "/settings/description";
    }

    @PostMapping(STUDY_SETTINGS + "banner/disable")
    public String studyBannerDisable(@CurrentUser Account account, @PathVariable String path) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        studyService.studyBannerDisable(studyUpdate);

        return "redirect:/study/" + fixPath(path) + "/settings/description";
    }
    // 배너 수정 끝

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

