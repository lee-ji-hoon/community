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

    // 좋아요 관련 내용
    @ResponseBody
    @RequestMapping(value = "/bookmark/add")
    public void addLikeLink(@CurrentUser Account account,
                            @RequestParam("id") Long id,
                            @RequestParam("sort") String sort) {
        bookmarkService.addBookmark(account, sort, id);
    }
}
