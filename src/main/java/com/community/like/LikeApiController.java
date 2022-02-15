package com.community.like;

import com.community.account.Account;
import com.community.account.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LikeApiController {
        private final LikeService likeService;

        @PostMapping("/board/detail/{boardId}/like")
        public ResponseEntity<String> addLike(
                @CurrentUser Account account,
                @PathVariable Long boardId) {

            boolean result = false;

            if (account != null) {
                result = likeService.addLike(account, boardId);
            }

            return result ?
                    new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
