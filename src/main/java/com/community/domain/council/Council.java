package com.community.domain.council;

import com.community.domain.account.Account;
import com.community.domain.reply.Reply;
import com.community.domain.bookmark.Bookmark;
import com.community.domain.likes.Likes;
import com.community.infra.aws.S3;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "cid")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Council {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cid;

    private String postSort;

    @NotBlank
    private String postTitle;

    @NotBlank
    private String postTarget;

    private String postLink;

    private String contactNum;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String postContent;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "postWriter")
    private Account postWriter;

    @OneToMany(mappedBy = "council", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "council", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "council", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "council", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3> imageList = new ArrayList<>();

    private Integer pageView;

    private LocalDateTime eventStartDate;

    private LocalDateTime eventEndDate;

    private LocalDateTime applyPeriodStartDate;

    private LocalDateTime applyPeriodEndDate;

    private LocalDateTime uploadTime;
}
