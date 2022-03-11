package com.community.council;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CouncilController {

    private final CouncilRepository councilRepository;
    private final CouncilService councilService;

    @GetMapping("/council")
    public String council(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new CouncilForm());
        model.addAttribute(councilService);
        return "council/council-posts";
    }

    @PostMapping("/council/detail")
    public String createNewPost(@Valid CouncilForm councilForm, Errors errors, RedirectAttributes redirectAttributes, @CurrentUser Account account) {
        if (errors.hasErrors()) {
            return "council/council-posts";
        }
        Council savedPost = councilService.saveNewPosts(councilForm, account);
        redirectAttributes.addAttribute("cid", savedPost.getCid());
        return "redirect:/council/detail/{cid}";
    }

    @GetMapping("/council/detail/{cid}")
    public String councilDetail(@CurrentUser Account account, @PathVariable long cid, Model model) {
        model.addAttribute(account);
        return "council/detail";
    }
}
