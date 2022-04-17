package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.graduation.GraduationType;
import com.community.web.dto.GraduationForm;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class GraduationController {

    @GetMapping("/graduation")
    public String graduationListView(@CurrentUser Account account, Model model) {

        model.addAttribute(account);

        return "graduation/graduation-list";
    }

    @GetMapping("/graduation-form")
    public String graduationForm(@CurrentUser Account account, Model model) {

        model.addAttribute(account);
        model.addAttribute(new GraduationForm());
        return "graduation/graduation-form";
    }

    @PostMapping("/graduation/register")
    public String graduationFormSubmit(Model model, @Valid GraduationForm graduationForm) {

        switch (graduationForm.getGraduationType()) {

            case WEB :
                graduationForm.setGraduationType(GraduationType.WEB);
                break;
            case MOBILE: :
                graduationForm.setGraduationType(GraduationType.MOBILE);
                break;
            case VR_AR: : graduationForm.setGraduationType(GraduationType.VR_AR); break;
            case VIDEO: : graduationForm.setGraduationType(GraduationType.VIDEO); break;

        }

        return "redirect:graduation/";
    }
}
