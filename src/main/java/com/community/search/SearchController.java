package com.community.search;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.form.BoardForm;
import com.community.board.repository.BoardRepository;
import com.community.council.Council;
import com.community.council.CouncilRepository;
import com.community.market.Market;
import com.community.market.MarketRepository;
import com.community.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.error.Mark;

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


    @GetMapping("/search/lists")
    public String listPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new SearchForm());
        return "search/search-form";
    }

    @PostMapping("/search/lists")
    public String searchPosts(@CurrentUser Account account, SearchForm searchForm, Model model) {
        log.info("검색 조건 : " + searchForm.getSearchType());
        log.info("검색 키워드 : " + searchForm.getKeyword());
        String keyword = searchForm.getKeyword();

        HashMap<Long, String> map = new HashMap<>();

        if (searchForm.getSearchType().equals("커뮤니티")) {
            List<Board> boardList = new ArrayList<>();
            List<Board> searchContent = boardRepository.findByContentContainingOrderByUploadTimeDesc(keyword);
            List<Board> searchTitle = boardRepository.findByTitleContainingOrderByUploadTimeDesc(keyword);
            boardList.addAll(searchContent);
            boardList.addAll(searchTitle);

            for (Board board : boardList) {
                map.put(board.getBid(), board.getTitle());
            }
        }
        if (searchForm.getSearchType().equals("학생회")) {
            List<Council> councilList = new ArrayList<>();
            List<Council> searchContent = councilRepository.findByPostContentContainingOrderByUploadTimeDesc(keyword);
            List<Council> searchTitle = councilRepository.findByPostTitleContainingOrderByUploadTimeDesc(keyword);
            councilList.addAll(searchContent);
            councilList.addAll(searchTitle);

            for (Council council : councilList) {
                map.put(council.getCid(), council.getPostTitle());
            }
        }
        if (searchForm.getSearchType().equals("장터")) {
            List<Market> marketList = new ArrayList<>();
            List<Market> searchContent = marketRepository.findByItemDetailContainingOrderByItemUploadTimeDesc(keyword);
            List<Market> searchTitle = marketRepository.findByItemNameContainingOrderByItemUploadTimeDesc(keyword);
            marketList.addAll(searchContent);
            marketList.addAll(searchTitle);

            for (Market market : marketList) {
                map.put(market.getMarketId(), market.getItemName());
            }
        }
        model.addAttribute(account);
        model.addAttribute("result", map);
        model.addAttribute("searchType", searchForm.getSearchType());
        model.addAttribute("keyword", searchForm.getKeyword());
        model.addAttribute(new SearchForm());

        return "search/search-form";
    }
}
