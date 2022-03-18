package com.community.search;

import com.community.account.CurrentUser;
import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.form.BoardForm;
import com.community.board.repository.BoardRepository;
import com.community.council.Council;
import com.community.council.CouncilRepository;
import com.community.market.MarketRepository;
import com.community.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    /*@ResponseBody
    @RequestMapping(value = "/board/detail/update")
    public String boardUpdate(BoardForm boardForm, @CurrentUser Account account,
                              @RequestParam(value = "bid") String bid,
                              @RequestParam(value = "boardTitle") String boardTitle,
                              @RequestParam(value = "subBoardTitle") String subBoardTitle,
                              @RequestParam(value = "title") String title,
                              @RequestParam(value = "subTitle") String subTitle,
                              @RequestParam(value = "content") String content) {
        Long boardId = Long.valueOf(bid);
        Board board = boardRepository.findByBid(boardId);
        String message = null;
        if (account.getId().equals(board.getWriter().getId())) {
            boardForm.setBoardTitle(boardTitle);
            boardForm.setSubBoardTitle(subBoardTitle);
            boardForm.setTitle(title);
            boardForm.setSubTitle(subTitle);
            boardForm.setContent(content);
            boardService.updateBoard(boardId, boardForm);
            message = "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                    "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                    "        <i class=\"icon-feather-x\"></i>\n" +
                    "    </button>\n" +
                    "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                    "    <p class=\"text-white text-opacity-75\">게시물이 수정되었습니다.</p>\n" +
                    "</div>";
            return message;
        }
        log.info("잘못된 게시물 수정 요청 : bid = " + boardId + " accountId = " + account.getId());
        message = "잘못된 요청입니다.";
        return message;
    }*/

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
            List<Council> searchContent = councilRepository.findByPostTitleContainingOrderByUploadTimeDesc(keyword);
            List<Council> searchTitle = councilRepository.findByPostContentContainingOrderByUploadTimeDesc(keyword);
            councilList.addAll(searchContent);
            councilList.addAll(searchTitle);

            for (Council council : councilList) {
                map.put(council.getCid(), council.getPostTitle());
            }
        }
        model.addAttribute(account);
        model.addAttribute("result", map);
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
