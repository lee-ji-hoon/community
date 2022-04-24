package com.community.web.controller;

import com.community.domain.account.CurrentUser;
import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import com.community.domain.board.ReplyRepository;
import com.community.domain.bookmark.Bookmark;
import com.community.domain.bookmark.BookmarkRepository;
import com.community.domain.likes.LikeRepository;
import com.community.domain.likes.Likes;
import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.service.BoardService;
import com.community.service.ReplyService;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import com.community.service.CouncilService;
import com.community.web.dto.CouncilForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CouncilController {

    private final CouncilRepository councilRepository;
    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;
    private final S3Repository s3Repository;

    private final CouncilService councilService;
    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping("/council/{type}/search")
    public String councilSearch(String keyword, @CurrentUser Account account, Model model,
                                @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                        direction = Sort.Direction.ASC) Pageable pageable,
                                @PathVariable String type) {

        Page<Council> searchCouncilResult = councilRepository.findByPostSortAndPostTitleContainingOrPostContentContainingOrderByUploadTimeDesc(type, keyword, keyword, pageable);

        model.addAttribute("searchCouncilResult", searchCouncilResult);
        model.addAttribute("pageNo", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("replyService", replyService);
        model.addAttribute("councilService", councilService);
        model.addAttribute(type);
        model.addAttribute(account);

        return "council/council-search";

    }

    @GetMapping("/council/{type}")
    public String councilRecent(@CurrentUser Account account, Model model,
                                @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                        direction = Sort.Direction.ASC) Pageable pageable,
                                @PathVariable String type) {
        switch (type) {
            case "notice" :
                Page<Council> councilNotice = councilRepository.findByPostSortOrderByUploadTimeDesc("공지", pageable);
                model.addAttribute("councilType", councilNotice);
                break;
            case "event" :
                Page<Council> councilEvent = councilRepository.findByPostSortOrderByUploadTimeDesc("행사", pageable);
                model.addAttribute("councilType", councilEvent);
                break;
        }

        model.addAttribute("pageNo", page);
        model.addAttribute(type);
        model.addAttribute(account);
        model.addAttribute(new CouncilForm());
        model.addAttribute(councilService);
        model.addAttribute(replyService);
        model.addAttribute(boardService);

        return "council/councils";
    }

    @ResponseBody
    @RequestMapping(value = "/council-new", method = RequestMethod.POST)
    public Long createNewPost(@CurrentUser Account account,
                              @RequestParam(value = "article_file") List<MultipartFile> multipartFile,
                              @RequestParam(value = "postSort", required = false) String postSort,
                              @RequestParam(value = "postTarget", required = false) String postTarget,
                              @RequestParam(value = "postTitle", required = false) String postTitle,
                              @RequestParam(value = "postLink", required = false) String postLink,
                              @RequestParam(value = "contactNum", required = false) String contactNum,
                              @RequestParam(value = "applyPeriodStartDate", required = false) String applyPeriodStartDate,
                              @RequestParam(value = "applyPeriodEndDate", required = false) String applyPeriodEndDate,
                              @RequestParam(value = "eventStartDate", required = false) String eventStartDate,
                              @RequestParam(value = "eventEndDate", required = false) String eventEndDate,
                              @RequestParam(value = "postContent", required = false) String postContent) {
        LocalDate APSD = LocalDate.parse(applyPeriodStartDate, DateTimeFormatter.ISO_DATE);
        LocalDate APED = LocalDate.parse(applyPeriodEndDate, DateTimeFormatter.ISO_DATE);
        LocalDate ESD = LocalDate.parse(eventStartDate, DateTimeFormatter.ISO_DATE);
        LocalDate EED = LocalDate.parse(eventEndDate, DateTimeFormatter.ISO_DATE);
        Council newCouncilPost = councilService.saveNewPosts(multipartFile, account, postSort, postTarget,
                postTitle, postLink, contactNum,
                APSD, APED, ESD, EED, postContent);
        return newCouncilPost.getCid();
    }

    @ResponseBody
    @RequestMapping(value = "/council/image/delete", method = RequestMethod.POST)
    public ResponseEntity councilDeleteImage(@RequestParam(value = "imageName") String imageName) {
        S3 s3 = s3Repository.findByImageName(imageName);

        councilService.deleteImage(s3);

        if(s3 != null) ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/council/detail/{cid}")
    public String councilDetail(@CurrentUser Account account, @PathVariable long cid, Model model,
                                HttpServletRequest request, HttpServletResponse response) {
        Council currentCouncil = councilRepository.findByCid(cid);

        councilService.viewUpdate(cid, request, response);
        Optional<Bookmark> existBookmark = bookmarkRepository.findByAccountAndCouncil(account, currentCouncil);

        Optional<Likes> existLike = likeRepository.findByAccountAndCouncil(account, currentCouncil);

        List<Reply> replies = replyRepository.findAllByCouncilOrderByUploadTimeDesc(currentCouncil);
        model.addAttribute(currentCouncil);
        model.addAttribute("bookmark", existBookmark);
        model.addAttribute("likes", existLike);
        model.addAttribute(account);
        model.addAttribute(boardService);
        model.addAttribute(councilService);
        model.addAttribute(new CouncilForm());
        model.addAttribute("reply", replies);
        model.addAttribute("replyService", replyService);
        return "council/council-detail";
    }

    @ResponseBody
    @RequestMapping(value = "/council/{id}/update", method = RequestMethod.POST)
    public ResponseEntity councilUpdate(@PathVariable Long id,
                                           @RequestParam(value = "article_file") List<MultipartFile> multipartFile,
                                           @RequestParam(value = "postSort", required = false) String postSort,
                                           @RequestParam(value = "postTarget", required = false) String postTarget,
                                           @RequestParam(value = "postTitle", required = false) String postTitle,
                                           @RequestParam(value = "postLink", required = false) String postLink,
                                           @RequestParam(value = "contactNum", required = false) String contactNum,
                                           @RequestParam(value = "applyPeriodStartDate", required = false) String applyPeriodStartDate,
                                           @RequestParam(value = "applyPeriodEndDate", required = false) String applyPeriodEndDate,
                                           @RequestParam(value = "eventStartDate", required = false) String eventStartDate,
                                           @RequestParam(value = "eventEndDate", required = false) String eventEndDate,
                                           @RequestParam(value = "postContent", required = false) String postContent) {
        LocalDate APSD = LocalDate.parse(applyPeriodStartDate, DateTimeFormatter.ISO_DATE);
        LocalDate APED = LocalDate.parse(applyPeriodEndDate, DateTimeFormatter.ISO_DATE);
        LocalDate ESD = LocalDate.parse(eventStartDate, DateTimeFormatter.ISO_DATE);
        LocalDate EED = LocalDate.parse(eventEndDate, DateTimeFormatter.ISO_DATE);

        Optional<Council> byId = councilRepository.findById(id);
        Council council = byId.get();

        councilService.updateCouncil(council, multipartFile, postSort, postTarget,
                postTitle, postLink, contactNum,
                APSD, APED, ESD, EED, postContent);
        return ResponseEntity.ok().build();
    }

    // 게시글 수정 후 {boardId}로 리다이렉트
    /*@ResponseBody
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
    }*/

    @GetMapping("/council/detail/delete/{councilId}")
    public String deleteCouncil(@PathVariable Long councilId, @CurrentUser Account account) {
        Council council = councilRepository.findByCid(councilId);
        if (!council.getPostWriter().getId().equals(account.getId())) {
            return "error-page";
        }
        councilService.deleteCouncil(council);
        return "redirect:/council/notice";
    }
}
