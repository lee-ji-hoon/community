package com.community.web.dto;

import com.community.domain.account.Account;
import lombok.Data;

@Data
public class ProfileImgForm {

    private String profileImage;

    public ProfileImgForm(Account account) {
        this.profileImage = account.getProfileImage();
    }
}
