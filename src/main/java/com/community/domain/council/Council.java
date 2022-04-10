package com.community.domain.council;

import com.community.domain.account.Account;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    // bucket object 이름
    private String fileName;

    // 실제 접근 주소
    private String filePath;

    private Integer pageView;

    private LocalDate eventStartDate;

    private LocalDate eventEndDate;

    private LocalDate applyPeriodStartDate;

    private LocalDate applyPeriodEndDate;

    private LocalDateTime uploadTime;
}
