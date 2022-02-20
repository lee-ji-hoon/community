package com.community.board;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
