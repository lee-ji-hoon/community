package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.CurrentUser;
import com.community.domain.council.Council;
import com.community.domain.council.CouncilRepository;
import com.community.infra.alarm.LikeCreatePublish;
import com.community.domain.board.Board;
import com.community.domain.board.BoardRepository;
import com.community.domain.likes.Likes;
import com.community.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LikeApiController {
    private final LikeService likeService;

    private final BoardRepository boardRepository;
    private final CouncilRepository councilRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    // 좋아요 관련 내용
    @ResponseBody
    @RequestMapping(value = "/like/add")
    public void addLikeLink(@RequestParam("like_postId") Long like_postId,
                           @RequestParam("like_postSort") String like_postSort,
                           @CurrentUser Account account){
        log.info("좋아요 호출");
        Likes likes = likeService.addLike(account, like_postSort, like_postId);
        switch (like_postSort) {
            case "board":
                Board currentBoard = boardRepository.findById(like_postId).get();
                if(!currentBoard.getWriter().getId().equals(account.getId())) {
                    log.info("board 좋아요 알림 이벤트 실행");
                    applicationEventPublisher.publishEvent(new LikeCreatePublish(likes, account));
                }
                break;

            case "council":
                Council currentCouncil = councilRepository.findByCid(like_postId);
                if(!currentCouncil.getPostWriter().getId().equals(account.getId())) {
                    log.info("council 좋아요 알림 이벤트 실행");
                    applicationEventPublisher.publishEvent(new LikeCreatePublish(likes, account));
                }
                break;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/like/delete")
    public void removeLikeLink(@RequestParam("like_postId") Long like_postId,
                              @RequestParam("like_postSort") String like_postSort,
                              @CurrentUser Account account){
        log.info("좋아요 취소 호출");
        likeService.deleteLike(account, like_postSort, like_postId);

    }
}
