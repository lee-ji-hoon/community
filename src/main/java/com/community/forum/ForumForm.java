package com.community.forum;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class ForumForm {
    @NotBlank
    @Length(min = 5, max = 30)
    private String postTitle;

    private String postSubtitle;

    @NotBlank
    private String postContent;

    private String forumTitle;

    private String forumTitleSub;

    private String[] keywordList;
}
