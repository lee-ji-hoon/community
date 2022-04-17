package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.graduation.Graduation;
import com.community.domain.graduation.GraduationRepository;
import com.community.service.GraduationService;
import com.community.web.dto.GraduationForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class GraduationController {

    private final ModelMapper modelMapper;
    private final GraduationService graduationService;
    private final GraduationRepository graduationRepository;

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

    @PostMapping("/graduation-new")
    public String graduationFormSubmit(@Valid GraduationForm graduationForm,
                                       @CurrentUser Account account) {

        Graduation newGraduation = graduationService.createNewGraduation(modelMapper.map(graduationForm, Graduation.class), account);

        return "redirect:graduation/" + newGraduation.getId();
    }

    @GetMapping("/graduation/{path}")
    public String graduationView(Model model, @PathVariable Long path,
                                 @CurrentUser Account account) {

        Optional<Graduation> byId = graduationRepository.findById(path);

        model.addAttribute(account);

        return "graduation/graduation-view";
    }
}
