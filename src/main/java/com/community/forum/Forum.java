package com.community.forum;

import com.community.account.entity.Account;
import com.community.board.entity.Board;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "bid")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long fid;

    private String forumTitle;

    private String forumTitleSub;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Account postWriter;

    private String postTitle;

    private String postSubtitle;

    private String keywordList;

    private LocalDateTime postUploadTime;

    @Column(columnDefinition = "TEXT")
    private String postContent;

    private Boolean isReported;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "answerer_id")
    private Account postAnswerer;

    private LocalDateTime answerUploadTime;

    @Column(columnDefinition = "TEXT")
    private String answerContent;

}
