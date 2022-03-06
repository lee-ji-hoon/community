package com.community.forum;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;
    private final BoardService boardService;
    private final ForumRepository forumRepository;

    @GetMapping("/forum")
    public String forumMainPage(@CurrentUser Account account, Model model) {
        List<Forum> recentlyPosts = forumRepository.findAllTop5ByIsReportedOrderByPostUploadTime(false);

        model.addAttribute(account);
        model.addAttribute(forumService);
        model.addAttribute("top5Posts", recentlyPosts);
        return "forum/forums";
    }

    @GetMapping("/forum/detail")
    public String forumDetail() {
        return "forum/forum-detail";
    }

    @GetMapping("/forum/write")
    public String forumWrite(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new ForumForm());
        return "forum/forum-write";
    }

    @PostMapping("/forum/write")
    public String createNewForum(ForumForm forumForm, @CurrentUser Account account, RedirectAttributes redirectAttributes) {
        Forum savedForum = forumService.saveNewForum(forumForm, account);
        redirectAttributes.addAttribute("fid", savedForum.getFid());

        return "redirect:/forum/detail/{fid}";
    }

    @GetMapping("/forum/detail/{fid}")
    public String detailForum(@PathVariable Long fid, @CurrentUser Account account, Model model) {
        model.addAttribute(boardService);
        model.addAttribute(account);
        model.addAttribute("forum", forumRepository.findByFid(fid));
        return "forum/forum-detail";
    }
}
