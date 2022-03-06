package com.community.forum;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.form.BoardForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ForumService {

    private final ForumRepository forumRepository;

    public Forum saveNewForum(ForumForm forumForm, Account account) {

        Forum forum = Forum.builder()
                .forumTitle(forumForm.getForumTitle())
                .forumTitleSub(forumForm.getForumTitleSub())
                .postTitle(forumForm.getPostTitle())
                .postSubtitle(forumForm.getPostSubtitle())
                .postWriter(account)
                .isReported(false)
                .postContent(forumForm.getPostContent())
                .postUploadTime(LocalDateTime.now())
                .build();
        return forumRepository.save(forum);
    }

    public List<Forum> postsTitleList(String boardTitle) {
        return forumRepository.findAllByIsReportedAndForumTitleOrderByPostUploadTimeDesc(false, boardTitle);
    }
}
