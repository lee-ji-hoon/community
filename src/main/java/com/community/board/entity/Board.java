package com.community.board.entity;

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
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long bid;

    private String boardTitle;

    @NotBlank
    private String title;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotBlank
    private String writer;

    private Long writerId;

    private Integer pageView;

    private LocalDateTime uploadTime;

    private LocalDateTime updateTime;
}
