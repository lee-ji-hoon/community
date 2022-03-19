package com.community.profile.form;

import com.community.account.entity.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationsForm {

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

    public NotificationsForm(Account account) {
        this.studyCreatedByEmail = account.isStudyCreatedByEmail();
        this.studyCreatedByWeb = account.isStudyCreatedByWeb();
        this.studyUpdatedByEmail = account.isStudyUpdatedByEmail();
        this.studyUpdatedByWeb = account.isStudyUpdatedByWeb();
    }
}
