package com.community.council;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import com.community.board.form.BoardForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CouncilService {

    private final CouncilRepository councilRepository;

    public Council saveNewPosts(CouncilForm councilForm, Account account) {
        log.info("eventStartDate : " + councilForm.getEventStartDate());
        log.info("eventEndDate : " + councilForm.getEventEndDate());
        Council council = Council.builder()
                .postTitle(councilForm.getPostTitle())
                .postSort(councilForm.getPostSort())
                .postContent(councilForm.getPostContent())
                .postWriter(account)
                .pageView(0)
                .eventStartDate(councilForm.getEventStartDate())
                .eventEndDate(councilForm.getEventEndDate())
                .build();
        log.info("eventStartDate : " + council.getEventStartDate());
        log.info("eventEndDate : " + council.getEventEndDate());
        return councilRepository.save(council);
    }

    public List<Council> postSort(String sort) {
        return councilRepository.findAllByPostSortOrderByEventEndDateDesc(sort);
    }

    public LocalDate nowDate() {
        return LocalDate.now();
    }
}
