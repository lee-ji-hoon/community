package com.community;

import com.community.account.entity.Account;
import com.community.account.CurrentUser;
import com.community.board.entity.Board;
import com.community.board.entity.Reply;
import com.community.board.repository.BoardRepository;
import com.community.board.repository.ReplyRepository;
import com.community.study.Study;
import com.community.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final StudyRepository studyRepository;


    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
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

        model.addAttribute("b_size",boardList.size());
        model.addAttribute("b_today",todayBoardList.size());
        model.addAttribute("r_size",replyList.size());
        model.addAttribute("r_today",todayReplyList.size());
        model.addAttribute("s_size",studyList.size());
        model.addAttribute("s_today",todayStudyList.size());

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login-form";
    }

}
