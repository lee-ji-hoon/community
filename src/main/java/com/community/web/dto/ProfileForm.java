package com.community.web.dto;

import com.community.domain.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileForm {

    private String bio;

    private String url;

    private String occupation;

    private String location;

    private String profileImage;

    private String major;

    private String grade;

    public ProfileForm(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
        this.profileImage = account.getProfileImage();
        this.major = account.getMajor();
        this.grade = account.getGrade();
    }
}
