package com.community.domain.board;

import com.community.domain.account.Account;
import com.community.domain.bookmark.Bookmark;
import com.community.domain.likes.Likes;
import com.community.domain.reply.Reply;
import com.community.infra.aws.S3;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "bid")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bid;

    //post_sort
    private String boardTitle;

    //post_sub_sort
    private String subBoardTitle;

    @NotBlank
    //post_title
    private String title;

    //post_sub_title
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
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Account writer;

    private Integer pageView;

    private LocalDateTime uploadTime;

    private LocalDateTime updateTime;
}
