package com.community.web.dto;

import com.community.domain.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlarmForm {

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

    public AlarmForm(Account account) {
        this.studyCreatedByEmail = account.isStudyCreatedByEmail();
        this.studyCreatedByWeb = account.isStudyCreatedByWeb();
        this.studyUpdatedByEmail = account.isReplyByMeetings();
        this.studyUpdatedByWeb = account.isReplyByPost();
    }
}
