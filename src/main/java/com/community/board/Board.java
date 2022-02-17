package com.community.board;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue
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
    // 위에 작성자랑 합쳐서 찾아오는 방식으로 할 수 있을 것 같긴한데 너무 졸려서 우선 임시로 해뒀어
    // 작성자로 안받아온 이유는 작성자는 내가 닉네임을 수정이 가능해서
    // 나중에 수정한 뒤에도 삭제가 될 수 있게끔 id로 받아왔어

    private Integer pageView;

    private LocalDateTime uploadTime;

    private LocalDateTime updateTime;
}
