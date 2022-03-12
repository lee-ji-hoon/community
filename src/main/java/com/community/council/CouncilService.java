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
        Council council = Council.builder()
                .postTitle(councilForm.getPostTitle())
                .postLink(councilForm.getPostLink())
                .postContent(councilForm.getPostContent())
                .postTarget(councilForm.getPostTarget())
                .postSort(councilForm.getPostSort())
                .contactNum(councilForm.getContactNum())
                .postWriter(account)
                .pageView(0)
                .eventStartDate(councilForm.getEventStartDate())
                .eventEndDate(councilForm.getEventEndDate())
                .applyPeriodStartDate(councilForm.getApplyPeriodStartDate())
                .applyPeriodEndDate(councilForm.getApplyPeriodEndDate())
                .uploadTime(LocalDateTime.now())
                .build();
        return councilRepository.save(council);
    }

    public List<Council> postSort(String sort) {
        return councilRepository.findAllByPostSortOrderByEventEndDateDesc(sort);
    }

    public LocalDate nowDate() {
        return LocalDate.now();
    }
}
