package com.community.profile.form;

import com.community.account.entity.Account;
import lombok.Data;

@Data
public class ProfileImgForm {

    private String profileImage;

    public ProfileImgForm(Account account) {
        this.profileImage = account.getProfileImage();
    }
}
