package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.board.Reply;
import com.community.domain.board.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ManageController {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @GetMapping("/manager/{type}")
    public String boardSearch(@CurrentUser Account account, Model model,
                              @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                              @PageableDefault(size = 5, page = 0, sort = "uploadTime",
                                      direction = Sort.Direction.ASC) Pageable pageable,
                              @PathVariable String type) {
        switch (type) {
            case "board" :
                Page<Board> boardReportedLists = boardRepository.findByIsReportedOrderByUploadTimeDesc(true, pageable);
                model.addAttribute("boardReportedLists", boardReportedLists);
                break;
            case "reply" :
                Page<Reply> replyReportedLists = replyRepository.findByIsReportedOrderByUploadTimeDesc(true, pageable);
                model.addAttribute("replyReportedLists", replyReportedLists);
                break;
        }
        model.addAttribute("pageNo", page);
        model.addAttribute(type);
        model.addAttribute(account);

        return "manager/manager-main";

    }
}
