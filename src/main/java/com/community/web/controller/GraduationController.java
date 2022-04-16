package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.web.dto.GraduationForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    /*@PostMapping("/graduation/register")
    public String graduationFormSubmit() {

        return "redirect:graduation/" + path;
    }*/
}
