package com.community.board.entity;

import com.community.account.entity.Account;
import com.community.like.Likes;
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

    private String boardTitle;

    private String subBoardTitle;

    @NotBlank
    private String title;

    private String subTitle;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

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
