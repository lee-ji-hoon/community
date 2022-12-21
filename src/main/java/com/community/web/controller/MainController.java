package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.board.Reply;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.ReplyRepository;
import com.community.domain.study.StudyRepository;
import com.community.service.BoardService;
import com.community.service.ReplyService;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import com.community.service.LikeService;
import com.community.domain.study.Study;
import com.community.web.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {
    private final BoardRepository boardRepository;
    private final CouncilRepository councilRepository;
    private final ReplyRepository replyRepository;
    private final StudyRepository studyRepository;

    private final ReplyService replyService;
    private final BoardService boardService;
    private final LikeService likeService;


    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
            List<Study> enrollStudy = studyRepository.findByMembersContainingOrderByPublishedDateTimeDesc(account);
            List<Study> myStudy = studyRepository.findByManagersContainingOrderByPublishedDateTimeDesc(account);
            model.addAttribute("enrollStudy", enrollStudy);
            model.addAttribute("myStudy", myStudy);
        }
        List<Board> boardList = boardRepository.findAll();
        List<Board> todayBoardList = new ArrayList<>();
        for (Board b_list : boardList) {
            if (b_list.getUploadTime().toLocalDate().equals(LocalDate.now())) {
                todayBoardList.add(b_list);
            }
        }
        List<Reply> replyList = replyRepository.findAll();
        List<Reply> todayReplyList = new ArrayList<>();
        for (Reply r_list : replyList) {
            if (r_list.getUploadTime().toLocalDate().equals(LocalDate.now())) {
                todayReplyList.add(r_list);
            }
        }

        List<Study> studyList = studyRepository.findAll();
        List<Study> todayStudyList = new ArrayList<>();
        for (Study s_list : studyList) {
            if (s_list.getPublishedDateTime().toLocalDate().equals(LocalDate.now())) {
                todayStudyList.add(s_list);
            }
        }

        List<Council> noticeLists = councilRepository.findTop4ByPostSortOrderByUploadTimeDesc("공지");

        List<Board> top5Board = boardService.top5BoardLists();

        model.addAttribute("board", top5Board);

        model.addAttribute("now", LocalDateTime.now());

        model.addAttribute("notice", noticeLists);
        model.addAttribute("service", boardService);
        model.addAttribute("replyService", replyService);
        model.addAttribute("likeService", likeService);

        model.addAttribute("b_size",boardList.size());
        model.addAttribute("b_today",todayBoardList.size());
        model.addAttribute("r_size",replyList.size());
        model.addAttribute("r_today",todayReplyList.size());
        model.addAttribute("s_size",studyList.size());
        model.addAttribute("s_today",todayStudyList.size());

        return "index";
    }

}
