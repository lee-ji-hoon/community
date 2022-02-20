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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyFormValidator studyFormValidator;
    private final ObjectMapper objectMapper;
    private final TagService tagService;

    private final TagRepository tagRepository;

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

    @GetMapping(STUDY_FORM_URL)
    public String newStudyForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new StudyForm());
        return STUDY_FORM_VIEW;
    }

    @PostMapping(STUDY_FORM_URL)
    public String newStudySubmit(@CurrentUser Account account, @Valid StudyForm studyForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return STUDY_FORM_VIEW;
        }

        Study newStudy = studyService.createNewStudy(modelMapper.map(studyForm, Study.class), account);
        return "redirect:/study/" + URLEncoder.encode(newStudy.getPath(), StandardCharsets.UTF_8);
    }

    @GetMapping(STUDY_PATH_VIEW)
    public String viewStudy(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study bypath = studyService.getPath(path);

        model.addAttribute(account);
        model.addAttribute(bypath);

        return "study/view";
    }

    @GetMapping(STUDY_PATH_VIEW + "/members")
    public String viewStudyMembers(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study bypath = studyService.getPath(path);

        model.addAttribute(account);
        model.addAttribute(bypath);

        return "study/members";
    }

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

        return ResponseEntity.badRequest().build();
    }

}
