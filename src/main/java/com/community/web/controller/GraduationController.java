package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.graduation.Graduation;
import com.community.domain.graduation.GraduationRepository;
import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.service.GraduationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.FacesWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GraduationController {

    private final GraduationService graduationService;
    private final GraduationRepository graduationRepository;

    @GetMapping("/graduation")
    public String graduationListView(@CurrentUser Account account, Model model,
                                     @PageableDefault(size = 9, page = 0, sort = "graduationDate",
                                             direction = Sort.Direction.ASC) Pageable pageable,
                                     @RequestParam(required = false, defaultValue = "0", value = "page") int page){
        int format = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
        Page<Graduation> graduationPage = graduationRepository.findAllGraduation(pageable);

        model.addAttribute(account);
        model.addAttribute("pageNo", page);
        model.addAttribute("projectList", graduationPage);
        model.addAttribute("now", format);


        return "graduation/graduation-list";
    }

    @GetMapping("/graduation/search-date/{date}")
    public String graduationListByDate(@CurrentUser Account account, Model model,
                                         @PathVariable int date,
                                         @PageableDefault(size = 9, page = 0, sort = "graduationDate",
                                             direction = Sort.Direction.ASC) Pageable pageable,
                                         @RequestParam(required = false, defaultValue = "0", value = "page") int page) {

        int format = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
        Page<Graduation> graduationPage = graduationRepository.findByGraduationDate(date, pageable);

        model.addAttribute(account);
        model.addAttribute("keyword", date);
        model.addAttribute("pageNo", page);
        model.addAttribute("href", "/graduation/search-date/");
        model.addAttribute("projectList", graduationPage);
        model.addAttribute("now", format);


        return "graduation/graduation-search";
    }

    @GetMapping("/graduation/search-keyword/{keyword}")
    public String graduationListByKeyword(@CurrentUser Account account, Model model,
                                          @PathVariable String keyword,
                                          @PageableDefault(size = 9, page = 0, sort = "graduationDate",
                                                  direction = Sort.Direction.ASC) Pageable pageable,
                                          @RequestParam(required = false, defaultValue = "0", value = "page") int page) {


        int format = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
        Page<Graduation> graduationPage = graduationRepository.findByGraduationType(keyword, pageable);

        model.addAttribute(account);
        model.addAttribute("keyword", keyword);
        model.addAttribute("href", "/graduation/search-keyword/");
        model.addAttribute("pageNo", page);
        model.addAttribute("projectList", graduationPage);
        model.addAttribute("now", format);


        return "graduation/graduation-search";
    }

    @GetMapping("/graduation-form")
    public String graduationForm(@CurrentUser Account account, Model model, RedirectAttributes redirectAttributes) {
        boolean emailVerified = account.isEmailVerified();

        log.info("email 체크 : {}", emailVerified);
        if (!emailVerified) {
            model.addAttribute(account);
            redirectAttributes.addFlashAttribute("emailVerifiedChecked", "이메일 인증 후에 사용 가능합니다.");

            return "redirect:/study";
        }

        int format = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));

        model.addAttribute(account);
        model.addAttribute("now", format);

        return "graduation/graduation-form";
    }

    @ResponseBody
    @RequestMapping(value = "/graduation-new", method = RequestMethod.POST)
    public Long graduationFormSubmit(@CurrentUser Account account,
                                       @RequestParam(value = "article_file") List<MultipartFile> multipartFile,
                                       @RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "teamName", required = false) String teamName,
                                       @RequestParam(value = "teamMember", required = false ) String teamMember,
                                       @RequestParam(value = "path", required = false) String path,
                                       @RequestParam(value = "graduationType", required = false) String graduationType,
                                       @RequestParam(value = "graduationDate", required = false) int graduationDate,
                                       @RequestParam(value = "description", required = false) String description) {
        Graduation newGraduationProject = graduationService.createNewGraduationProject(
                multipartFile, title,
                teamMember, teamName, path, graduationType,
                graduationDate, description, account
        );

        return newGraduationProject.getId();
    }

    @PostMapping("/graduation/{path}/delete")
    public String graduationDelete(@PathVariable Long path) {

        Graduation graduation = graduationRepository.findById(path).get();
        graduationService.deleteGraduation(graduation);

        return "redirect:/graduation";
    }

    @ResponseBody
    @RequestMapping(value = "/graduation/{id}/update", method = RequestMethod.POST)
    public ResponseEntity graduationUpdate(@PathVariable Long id,
                                           @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile,
                                           @RequestParam(value = "title", required = false) String title,
                                           @RequestParam(value = "teamName", required = false) String teamName,
                                           @RequestParam(value = "teamMember", required = false ) String teamMember,
                                           @RequestParam(value = "path", required = false) String path,
                                           @RequestParam(value = "graduationType", required = false) String graduationType,
                                           @RequestParam(value = "graduationDate", required = false) int graduationDate,
                                           @RequestParam(value = "description", required = false) String description) {

        Optional<Graduation> byId = graduationRepository.findById(id);
        Graduation graduation = byId.get();

        graduationService.updateGraduation(graduation, multipartFile, title,
                teamMember, teamName, path, graduationType,
                graduationDate, description);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/graduation/{path}")
    public String graduationView(Model model, @PathVariable Long path,
                                 @CurrentUser Account account) {

        Optional<Graduation> byId = graduationRepository.findById(path);

        String teamMember = byId.get().getTeamMember();
        String[] teamMemberArray = teamMember.split(",");
        List<String> teamMemberList = new ArrayList<>();

        teamMemberList.addAll(Arrays.asList(teamMemberArray));

        model.addAttribute("project", byId.get());
        model.addAttribute("teamMemberList", teamMemberList);
        model.addAttribute(account);

        return "graduation/graduation-view";
    }
}
