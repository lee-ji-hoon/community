package com.community.web.controller;

import com.community.domain.account.CurrentUser;
import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import com.community.domain.board.ReplyRepository;
import com.community.service.BoardService;
import com.community.service.ReplyService;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import com.community.service.CouncilService;
import com.community.web.dto.CouncilForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CouncilController {

    private final CouncilRepository councilRepository;
    private final ReplyRepository replyRepository;
    private final CouncilService councilService;
    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping("/council")
    public String council(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new CouncilForm());
        model.addAttribute(councilService);
        model.addAttribute(boardService);
        return "council/councils";
    }

    @PostMapping("/council/detail")
    public String createNewPost(@Valid CouncilForm councilForm, Errors errors, RedirectAttributes redirectAttributes, @CurrentUser Account account) {
        if (errors.hasErrors()) {
            return "council/councils";
        }
        Council savedPost = councilService.saveNewPosts(councilForm, account);
        redirectAttributes.addAttribute("cid", savedPost.getCid());
        return "redirect:/council/detail/{cid}";
    }

    @GetMapping("/council/detail/{cid}")
    public String councilDetail(@CurrentUser Account account, @PathVariable long cid, Model model,
                                HttpServletRequest request, HttpServletResponse response) {
        Council council = councilRepository.findByCid(cid);

        councilService.viewUpdate(cid, request, response);

        List<Reply> replies = replyRepository.findAllByCouncilOrderByUploadTimeDesc(council);
        model.addAttribute(council);
        model.addAttribute(account);
        model.addAttribute(boardService);
        model.addAttribute(councilService);
        model.addAttribute(new CouncilForm());
        model.addAttribute("reply", replies);
        model.addAttribute("replyService", replyService);
        return "council/council-detail";
    }

    // 게시글 수정 후 {boardId}로 리다이렉트
    @ResponseBody
    @RequestMapping(value = "/council/detail/update")
    public String boardUpdate(CouncilForm councilForm, @CurrentUser Account account,
                              @RequestParam(value = "cid") String cid,
                              @RequestParam(value = "postSort") String postSort,
                              @RequestParam(value = "postTarget") String postTarget,
                              @RequestParam(value = "postTitle") String postTitle,
                              @RequestParam(value = "postLink") String postLink,
                              @RequestParam(value = "contactNum") String contactNum,
                              @RequestParam(value = "applyPeriodStartDate") String applyPeriodStartDate,
                              @RequestParam(value = "applyPeriodEndDate") String applyPeriodEndDate,
                              @RequestParam(value = "eventStartDate") String eventStartDate,
                              @RequestParam(value = "eventEndDate") String eventEndDate,
                              @RequestParam(value = "postContent") String postContent) {
        LocalDate APSD = LocalDate.parse(applyPeriodStartDate, DateTimeFormatter.ISO_DATE);
        LocalDate APED = LocalDate.parse(applyPeriodEndDate, DateTimeFormatter.ISO_DATE);
        LocalDate ESD = LocalDate.parse(eventStartDate, DateTimeFormatter.ISO_DATE);
        LocalDate EED = LocalDate.parse(eventEndDate, DateTimeFormatter.ISO_DATE);
        Long councilId = Long.valueOf(cid);
        Council council = councilRepository.findByCid(councilId);
        String message = null;
        if (account.getId().equals(council.getPostWriter().getId())) {
            councilForm.setPostSort(postSort);
            councilForm.setPostTarget(postTarget);
            councilForm.setPostTitle(postTitle);
            councilForm.setPostLink(postLink);
            councilForm.setContactNum(contactNum);
            councilForm.setApplyPeriodStartDate(APSD);
            councilForm.setApplyPeriodEndDate(APED);
            councilForm.setEventStartDate(ESD);
            councilForm.setEventEndDate(EED);
            councilForm.setPostContent(postContent);
            councilService.updateCouncil(councilId, councilForm);
            message = "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                    "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                    "        <i class=\"icon-feather-x\"></i>\n" +
                    "    </button>\n" +
                    "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                    "    <p class=\"text-white text-opacity-75\">게시물이 수정되었습니다.</p>\n" +
                    "</div>";
            return message;
        }
        message = "잘못된 요청입니다.";
        return message;
    }

    @GetMapping("/council/detail/delete/{councilId}")
    public String deleteCouncil(@PathVariable Long councilId, @CurrentUser Account account) {
        Council council = councilRepository.findByCid(councilId);
        if (!council.getPostWriter().getId().equals(account.getId())) {
            return "error-page";
        }
        councilRepository.delete(council);
        return "redirect:/council";
    }
}
