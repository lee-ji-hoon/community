package com.community.domain.board;

import com.community.domain.account.Account;
import com.community.domain.base.BaseEntity;
import com.community.domain.bookmark.Bookmark;
import com.community.domain.likes.Likes;
import com.community.domain.reply.Reply;
import com.community.infra.aws.S3;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Board extends BaseEntity {

    //post_sort
    @Setter
    private String boardTitle;

    //post_sub_sort
    @Setter
    private String subBoardTitle;

    //post_title
    @NotBlank
    @Setter
    private String title;

    //post_sub_title
    @Setter
    private String subTitle;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3> imageList = new ArrayList<>();

    private Boolean isReported;

    private Integer reportCount;

    @NotBlank
    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Account writer;

    private Integer pageView;

    public void setBasicInfo(Account account) {
        this.isReported = false;
        this.pageView = 0;
        this.reportCount = 0;
        this.writer = account;
    }

    public void setReportReset() {
        this.reportCount = 0;
        this.isReported = false;
    }

    public void increasePageView() {
        this.pageView += 1;
    }

    public void increaseReportCount() {
        this.reportCount += 1;
    }

    public void setReported() {
        this.isReported = true;
    }
}
