package com.community.study;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.tag.Tag;
import com.community.tag.TagForm;
import com.community.tag.TagRepository;
import com.community.study.form.StudyDescriptionForm;
import com.community.study.form.StudyForm;
import com.community.study.validator.StudyFormValidator;
import com.community.tag.TagService;
import com.community.zone.Zone;
import com.community.zone.ZoneForm;
import com.community.zone.ZoneRepository;
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
    private final ZoneRepository zoneRepository;
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

    @GetMapping("/study")
    public String study(@CurrentUser Account account, Model model) {
        model.addAttribute("studyListPublished", studyRepository.findFirst9ByPublishedAndClosedOrderByPublishedDateTimeDesc(true, false));
        model.addAttribute("studyListClosed", studyRepository.findFirst9ByPublishedAndClosedOrderByIdDesc(false, false));
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

        String parameter = httpServletRequest.getParameter("startStudyDate");
        String parameter1 = httpServletRequest.getParameter("limitStudyDate");
        String parameter2 = httpServletRequest.getParameter("limitMemberDate");
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

        return "redirect:/study/" + fixPath(path) + "/members";
    }

    // 스터디 탈퇴
    @GetMapping(STUDY_PATH_VIEW + "/remove")
    public String removeStudy(@CurrentUser Account account, @PathVariable String path) {
        Study studyWithMembersByPath = studyRepository.findStudyWithMembersByPath(path);
        studyService.removeMember(studyWithMembersByPath, account);

        return "redirect:/study/" + fixPath(path) + "/members";
    }

    // 스터디 설명 수정 시작
    @GetMapping(STUDY_SETTINGS + "description")
    public String viewStudyForm(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        model.addAttribute(account);
        model.addAttribute(studyUpdate);
        model.addAttribute(modelMapper.map(studyUpdate, StudyDescriptionForm.class));
        return "study/settings/description";
    }

    @PostMapping(STUDY_SETTINGS + "description")
    public String updateStudyForm(@CurrentUser Account account, @PathVariable String path,
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

    // 배너 수정 시작
    @GetMapping(STUDY_SETTINGS + "banner")
    public String studyImageForm(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        model.addAttribute(account);
        model.addAttribute(studyUpdate);
        return "study/settings/banner";
    }

    @PostMapping(STUDY_SETTINGS + "banner")
    public String studyImageUpToDate(@CurrentUser Account account, @PathVariable String path, String image,
                                     Model model, RedirectAttributes redirectAttributes) {

        Study studyUpdate = studyService.getStudyUpdate(account, path);
        studyService.getStudyImage(studyUpdate, image);

        redirectAttributes.addFlashAttribute("message", "이미지가 수정됐습니다.");

        return "redirect:/study/" + fixPath(path) + "/settings/banner";
    }

    @PostMapping(STUDY_SETTINGS + "banner/enable")
    public String studyBannerEnable(@CurrentUser Account account, @PathVariable String path) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        studyService.studyBannerEnable(studyUpdate);

        return "redirect:/study/" + fixPath(path) + "/settings/banner";
    }

    @PostMapping(STUDY_SETTINGS + "banner/disable")
    public String studyBannerDisable(@CurrentUser Account account, @PathVariable String path) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        studyService.studyBannerDisable(studyUpdate);

        return "redirect:/study/" + fixPath(path) + "/settings/banner";
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
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));

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

    // 지역 수정 시작
    @GetMapping(STUDY_SETTINGS + "zones")
    public String studyZonesForm(@CurrentUser Account account, @PathVariable String path, Model model) throws JsonProcessingException {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        model.addAttribute(account);
        model.addAttribute(studyUpdate);

        model.addAttribute("zones", studyUpdate.getZones().stream()
                .map(Zone::toString).collect(Collectors.toList()));
        List<String> allZones = zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allZones));

        return "study/settings/zones";
    }

    @PostMapping(STUDY_SETTINGS + "zones/add")
    @ResponseBody
    public ResponseEntity addZone(@CurrentUser Account account, @PathVariable String path,
                                  @RequestBody ZoneForm zoneForm) {
        Study studyToUpdateZone = studyService.getStudyToUpdateZone(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        studyService.addZone(studyToUpdateZone, zone);
        return ResponseEntity.ok().build();
    }

    @PostMapping(STUDY_SETTINGS + "zones/remove")
    @ResponseBody
    public ResponseEntity removeZone(@CurrentUser Account account, @PathVariable String path,
                                     @RequestBody ZoneForm zoneForm) {
        Study studyToUpdateZone = studyService.getStudyToUpdateZone(account, path);
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        studyService.removeZone(studyToUpdateZone, zone);
        return ResponseEntity.ok().build();
    }
    // 지역 수정 끝

    /* 스터디 전체적인 설정 시작 */
    // 스터디 공개 설정 시작
    @GetMapping(STUDY_SETTINGS + "study")
    public String studyForm(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        model.addAttribute(account);
        model.addAttribute(studyUpdate);

        return "study/settings/study";
    }

    @PostMapping(STUDY_SETTINGS + "study/publish")
    public String studyPublish(@CurrentUser Account account, @PathVariable String path, RedirectAttributes redirectAttributes) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        studyService.publish(studyUpdate);
        redirectAttributes.addFlashAttribute("message", "스터디가 공개됐습니다.");

        return "redirect:/study/" + fixPath(path) + "/settings/study";
    }

    @PostMapping(STUDY_SETTINGS + "study/close")
    public String studyClose(@CurrentUser Account account, @PathVariable String path, RedirectAttributes redirectAttributes) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        studyService.close(studyUpdate);
        redirectAttributes.addFlashAttribute("message", "스터디가 종료됐습니다.");

        return "redirect:/study/" + fixPath(path) + "/settings/study";
    }
    // 스터디 공개 설정 끝

    // 스터디 인원 모집 시작
    @PostMapping(STUDY_SETTINGS + "study/recruit-publish")
    public String studyRecruitPublish(@CurrentUser Account account, @PathVariable String path, RedirectAttributes redirectAttributes) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);
        if (!studyUpdate.UpdateRecruiting()) {
            redirectAttributes.addFlashAttribute("message_danger", "30분에 한 번만 인원 모집 설정이 변경 가능합니다. 잠시 뒤에 이용해주시기 바랍니다.");
            return "redirect:/study/" + fixPath(path) + "/settings/study";
        }

        studyService.recruitPublish(studyUpdate);
        redirectAttributes.addFlashAttribute("message", "인원 모집이 시작됐습니다. 스터디원을 모집해보세요");

        return "redirect:/study/" + fixPath(path) + "/settings/study";
    }

    @PostMapping(STUDY_SETTINGS + "study/recruit-close")
    public String studyRecruitClose(@CurrentUser Account account, @PathVariable String path, RedirectAttributes redirectAttributes) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        if (!studyUpdate.UpdateRecruiting()) {
            redirectAttributes.addFlashAttribute("message_danger", "30분에 한 번만 인원 모집 설정이 변경 가능합니다. 잠시 뒤에 이용해주시기 바랍니다.");
            return "redirect:/study/" + fixPath(path) + "/settings/study";
        }
        studyService.recruitClose(studyUpdate);

        redirectAttributes.addFlashAttribute("message", "인원 모집이 종료됐습니다.");
        return "redirect:/study/" + fixPath(path) + "/settings/study";
    }
    // 스터디 인원 모집 끝

    // 스터디 이름 변경
    @PostMapping(STUDY_SETTINGS + "study/title")
    public String studyTitleUpdate(@CurrentUser Account account, @PathVariable String path, RedirectAttributes redirectAttributes, Model model, String newTitle) {
        Study studyToUpdateStatus = studyService.getStudyToUpdateStatus(account, path);
        if (!studyService.isValidTitle(newTitle)) {
            model.addAttribute(account);
            model.addAttribute(studyToUpdateStatus);
            model.addAttribute("studyTitleError", "스터디 이름들 다시 입력해주세요");
            return "study/settings/study";
        }

        studyService.updateStudyTitle(studyToUpdateStatus, newTitle);
        redirectAttributes.addFlashAttribute("message", "스터디 이름을 수정했습니다.");

        return "redirect:/study/" + fixPath(path) + "/settings/study";
    }

    // 스터디 삭제
    @PostMapping(STUDY_SETTINGS + "study/remove")
    public String removeStudy(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdateStatus(account, path);
        studyService.remove(study);
        return "redirect:/";
    }

    // 스터디 경로 수정
    @PostMapping(STUDY_SETTINGS + "study/path")
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
        return "redirect:/study/" + fixPath(newPath) + "/settings/study";

    }
    /* 스터디 전체적인 설정 끝 */


}

