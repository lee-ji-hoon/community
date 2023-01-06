package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import com.community.domain.market.Market;
import com.community.domain.market.MarketRepository;
import com.community.domain.study.Study;
import com.community.domain.study.StudyRepository;
import com.community.domain.tag.Tag;
import com.community.domain.tag.TagRepository;
import com.community.web.dto.SearchForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final BoardRepository boardRepository;
    private final CouncilRepository councilRepository;
    private final MarketRepository marketRepository;
    private final StudyRepository studyRepository;
    private final TagRepository tagRepository;

    @GetMapping("/search/lists/result")
    public String searchPostsGet(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        return "search/search-form";
    }

    @PostMapping("/search/lists")
    public String searchPosts(@CurrentUser Account account, SearchForm searchForm, RedirectAttributes redirectAttributes) {
        log.info("검색 조건 : " + searchForm.getSearchType());
        log.info("검색 키워드 : " + searchForm.getKeyword());
        String keyword = searchForm.getKeyword();

        HashMap<String, String> map = new HashMap<>();

        if (searchForm.getSearchType().equals("커뮤니티")) {
            List<Board> boardList = new ArrayList<>();
            List<Board> searchContent = boardRepository.findByContentContainingOrderByUploadTimeDesc(keyword);
            List<Board> searchTitle = boardRepository.findByTitleContainingOrderByUploadTimeDesc(keyword);
            boardList.addAll(searchContent);
            boardList.addAll(searchTitle);

            for (Board board : boardList) {
                map.put(String.valueOf(board.getId()), board.getTitle());
            }
        }
        if (searchForm.getSearchType().equals("학생회")) {
            List<Council> councilList = new ArrayList<>();
            List<Council> searchContent = councilRepository.findByPostContentContainingOrderByUploadTimeDesc(keyword);
            List<Council> searchTitle = councilRepository.findByPostTitleContainingOrderByUploadTimeDesc(keyword);
            councilList.addAll(searchContent);
            councilList.addAll(searchTitle);

            for (Council council : councilList) {
                map.put(String.valueOf(council.getCid()), council.getPostTitle());
            }
        }
        if (searchForm.getSearchType().equals("장터")) {
            List<Market> marketList = new ArrayList<>();
            List<Market> searchContent = marketRepository.findByItemDetailContainingOrderByUploadTimeDesc(keyword);
            List<Market> searchTitle = marketRepository.findByItemNameContainingOrderByUploadTimeDesc(keyword);
            marketList.addAll(searchContent);
            marketList.addAll(searchTitle);

            for (Market market : marketList) {
                map.put(String.valueOf(market.getMarketId()), market.getItemName());
            }
        }
        if (searchForm.getSearchType().equals("동아리")) {
            List<Study> studyList = new ArrayList<>();

            Tag tag = tagRepository.findByTitle(keyword);
            if (tag != null) {
                List<Study> searchTag = studyRepository.findByTagsContaining(tag);
                studyList.addAll(searchTag);
            }
            List<Study> searchContent = studyRepository.findByFullDescriptionContaining(keyword);
            List<Study> searchTitle = studyRepository.findByTitleContaining(keyword);

            studyList.addAll(searchContent);
            studyList.addAll(searchTitle);

            for (Study study : studyList) {
                map.put(study.getPath(), study.getTitle());
                log.info("study : " + study.getId());
            }
        }
        redirectAttributes.addFlashAttribute(account);
        redirectAttributes.addFlashAttribute("result", map);
        redirectAttributes.addFlashAttribute("searchType", searchForm.getSearchType());
        redirectAttributes.addFlashAttribute("keyword", searchForm.getKeyword());
        redirectAttributes.addFlashAttribute(new SearchForm());

        return "redirect:/search/lists/result";
    }
}