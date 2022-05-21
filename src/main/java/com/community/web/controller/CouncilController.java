package com.community.web.controller;

import com.community.domain.account.CurrentUser;
import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import com.community.domain.board.ReplyRepository;
import com.community.domain.bookmark.Bookmark;
import com.community.domain.bookmark.BookmarkRepository;
import com.community.domain.likes.LikeRepository;
import com.community.domain.likes.Likes;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
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

    private final CouncilService councilService;
    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping("/council/{type}/search")
    public String councilSearch(String keyword, @CurrentUser Account account, Model model,
                                @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                @PageableDefault(size = 8, page = 0, sort = "uploadTime",
                                        direction = Sort.Direction.ASC) Pageable pageable,
                                @PathVariable String type) {

        Page<Council> searchCouncilResult = councilRepository.findByPostSortOrPostTitleContainingOrPostContentContainingOrderByUploadTimeDesc(type, keyword, keyword, pageable);

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
                                @PageableDefault(size = 8, page = 0, sort = "uploadTime",
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
                              @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile,
                              @RequestParam(value = "postSort", required = false) String postSort,
                              @RequestParam(value = "postTarget", required = false) String postTarget,
                              @RequestParam(value = "postTitle", required = false) String postTitle,
                              @RequestParam(value = "postLink", required = false) String postLink,
                              @RequestParam(value = "contactNum", required = false) String contactNum,
                              @RequestParam(value = "applyPeriodStartDate", required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applyPeriodStartDate,
                              @RequestParam(value = "applyPeriodEndDate", required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applyPeriodEndDate,
                              @RequestParam(value = "eventStartDate", required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDate,
                              @RequestParam(value = "eventEndDate", required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDate,
                              @RequestParam(value = "postContent", required = false) String postContent) {
        Council newCouncilPost = councilService.saveNewPosts(multipartFile, account, postSort, postTarget,
                postTitle, postLink, contactNum,
                applyPeriodStartDate, applyPeriodEndDate, eventStartDate, eventEndDate, postContent);
        return newCouncilPost.getCid();
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
                                           @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile,
                                           @RequestParam(value = "postSort", required = false) String postSort,
                                           @RequestParam(value = "postTarget", required = false) String postTarget,
                                           @RequestParam(value = "postTitle", required = false) String postTitle,
                                           @RequestParam(value = "postLink", required = false) String postLink,
                                           @RequestParam(value = "contactNum", required = false) String contactNum,
                                           @RequestParam(value = "applyPeriodStartDate", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applyPeriodStartDate,
                                           @RequestParam(value = "applyPeriodEndDate", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applyPeriodEndDate,
                                           @RequestParam(value = "eventStartDate", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventStartDate,
                                           @RequestParam(value = "eventEndDate", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime eventEndDate,
                                           @RequestParam(value = "postContent", required = false) String postContent) {
        Optional<Council> byId = councilRepository.findById(id);
        Council council = byId.get();

        councilService.updateCouncil(council, multipartFile, postSort, postTarget,
                postTitle, postLink, contactNum,
                applyPeriodStartDate, applyPeriodEndDate, eventStartDate, eventEndDate, postContent);
        return ResponseEntity.ok().build();
    }

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
