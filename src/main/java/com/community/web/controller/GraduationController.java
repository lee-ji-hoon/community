package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.graduation.Graduation;
import com.community.domain.graduation.GraduationRepository;
import com.community.service.GraduationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GraduationController {

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
        return "graduation/graduation-form";
    }

    @ResponseBody
    @RequestMapping(value = "/graduation-new", method = RequestMethod.POST)
    public Long graduationFormSubmit(@CurrentUser Account account,
                                       @RequestParam("article_file") List<MultipartFile> multipartFile,
                                       @RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "teamName", required = false) String teamName,
                                       @RequestParam(value = "teamMember", required = false ) String teamMember,
                                       @RequestParam(value = "path", required = false) String path,
                                       @RequestParam(value = "graduationType", required = false) String graduationType,
                                       @RequestParam(value = "graduationDate", required = false) int graduationDate,
                                       @RequestParam(value = "description", required = false) String description) {
        for (MultipartFile file : multipartFile) {
            log.info("이미지 파일 : {}", file.getName());
        }

        Graduation newGraduationProject = graduationService.createNewGraduationProject(
                multipartFile, title,
                teamMember, teamName, path, graduationType,
                graduationDate, description, account
        );

        return newGraduationProject.getId();
    }

    @GetMapping("/graduation/{path}")
    public String graduationView(Model model, @PathVariable Long path,
                                 @CurrentUser Account account) {

        Optional<Graduation> byId = graduationRepository.findById(path);

        model.addAttribute("project",byId.get());
        model.addAttribute(account);

        return "graduation/graduation-view";
    }
}
