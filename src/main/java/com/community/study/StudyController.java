package com.community.study;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.study.form.StudyDescriptionForm;
import com.community.study.form.StudyForm;
import com.community.study.validator.StudyFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyFormValidator studyFormValidator;

    private static final String STUDY_FORM = "/study-form";
    private static final String STUDY_VIEW = "study/form";

    private static final String STUDY_PATH = "study/{path}";


    @InitBinder("studyForm")
    public void studyFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studyFormValidator);
    }

    @GetMapping(STUDY_FORM)
    public String newStudyForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new StudyForm());
        return STUDY_VIEW;
    }

    @PostMapping(STUDY_FORM)
    public String newStudySubmit(@CurrentUser Account account, @Valid StudyForm studyForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return STUDY_VIEW;
        }

        Study newStudy = studyService.createNewStudy(modelMapper.map(studyForm, Study.class), account);
        return "redirect:/study/" + URLEncoder.encode(newStudy.getPath(), StandardCharsets.UTF_8);
    }

    @GetMapping(STUDY_PATH)
    public String viewStudy(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study bypath = studyService.getPath(path);

        model.addAttribute(account);
        model.addAttribute(bypath);

        return "study/view";
    }

    @GetMapping(STUDY_PATH + "/members")
    public String viewStudyMembers(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study bypath = studyService.getPath(path);

        model.addAttribute(account);
        model.addAttribute(bypath);

        return "study/members";
    }

    @GetMapping("/study/{path}/settings/description")
    public String viewStudyForm(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study studyUpdate = studyService.getStudyUpdate(account, path);

        model.addAttribute(account);
        model.addAttribute(studyUpdate);
        model.addAttribute(modelMapper.map(studyUpdate, StudyDescriptionForm.class));
        return "study/settings/description";
    }

    @PostMapping("/study/{path}/settings/description")
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

    private String fixPath(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

}
