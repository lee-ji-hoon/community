package com.community.web.dto;

import com.community.domain.board.Board;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
public class BoardForm {
    @NotBlank
    @Length(min = 2, max = 30)
    private String title;

    private String subTitle;

    @NotBlank
    @Length(min = 2)
    private String content;

    private String boardTitle;

    private String subBoardTitle;

    public BoardForm(Board board) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardTitle = boardTitle;
        this.subBoardTitle = subBoardTitle;
    }
}
