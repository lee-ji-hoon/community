package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.bookmark.BookmarkRepository;
import com.community.domain.council.CouncilRepository;
import com.community.domain.likes.Likes;
import com.community.domain.market.MarketRepository;
import com.community.domain.study.StudyRepository;
import com.community.infra.alarm.LikeCreatePublish;
import com.community.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/bookmarks")
    public String bookmarkMain(@CurrentUser Account account, Model model) {

        model.addAttribute(account);
        return "bookmark/bookmarks";
    }

    @ResponseBody
    @RequestMapping(value = "/bookmark/add")
    public void addBookmarkAjax(@CurrentUser Account account,
                            @RequestParam("postId") Long postId,
                            @RequestParam("postSort") String postSort) {
        log.info("postId={}", postId);
        log.info("postSort={}", postSort);
        // postId = 각 게시물의 아이디를 가져옴
        // postSort = board, council, market, study를 가져옴
        bookmarkService.addBookmark(account, postSort, postId);
    }

    @ResponseBody
    @RequestMapping(value = "/bookmark/delete")
    public void deleteBookmarkAjax(@CurrentUser Account account,
                            @RequestParam("postId") Long postId,
                            @RequestParam("postSort") String postSort) {
        log.info("postId={}", postId);
        log.info("postSort={}", postSort);
        // postId = 각 게시물의 아이디를 가져옴
        // postSort = board, council, market, study를 가져옴
        bookmarkService.deleteBookmark(account, postSort, postId);
    }
}
