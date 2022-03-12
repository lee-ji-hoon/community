package com.community.council;

import com.community.account.entity.Account;
import com.community.board.entity.Reply;
import com.community.like.Likes;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.TABLE)
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

    private Integer pageView;

    private LocalDate eventStartDate;

    private LocalDate eventEndDate;

    private LocalDate applyPeriodStartDate;

    private LocalDate applyPeriodEndDate;
}
