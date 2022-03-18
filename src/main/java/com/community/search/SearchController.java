package com.community.search;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.repository.BoardRepository;
import com.community.council.CouncilRepository;
import com.community.market.MarketRepository;
import com.community.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final BoardRepository boardRepository;
    private final CouncilRepository councilRepository;
    private final MarketRepository marketRepository;
    private final StudyRepository studyRepository;


    @GetMapping("/search")
    public String listPage(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new SearchForm());
        return "search/search-form";
    }

    @PostMapping("/search")
    public String searchPosts(@CurrentUser Account account, SearchForm searchForm, Model model) {
        log.info("검색 조건 : " + searchForm.getSearchType());
        log.info("검색 키워드 : " + searchForm.getKeyword());
        String keyword = searchForm.getKeyword();

        /*if (searchForm.getSearchType().equals("커뮤니티")) {
            List<Board> search_result = boardRepository.findByContentContainingOrderByUploadTimeDesc(keyword);
            model.addAttribute(search_result);
        }*/

        model.addAttribute(account);
        model.addAttribute("searchType", searchForm.getSearchType());
        model.addAttribute(new SearchForm());

        return "search/search-form";
    }

    // 검색 기능
    /*@PostMapping("/board/search/{boardTitle}")
    public String searchPost(@PathVariable String boardTitle, SearchForm searchForm, Model model) {
        log.info("검색 조건 : " + searchForm.getSearchType());
        log.info("검색 키워드 : " + searchForm.getKeyword());
        List<Board> searchPosts = boardService.searchPosts(searchForm.getSearchType(), searchForm.getKeyword(), searchForm.getBoardTitle());

        model.addAttribute("board", searchPosts);
        model.addAttribute("accountRepo", accountRepository);
        model.addAttribute("service", boardService);
        model.addAttribute("likeService", likeService);
        model.addAttribute("replyService", replyService);
        model.addAttribute("bt", boardTitle);

        model.addAttribute(new SearchForm());
        return "board/board-list";
    }*/
}
