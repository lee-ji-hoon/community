package com.community.domain.inquire;

import com.community.domain.account.Account;
import com.community.infra.aws.S3;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class InquireAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "answer_id")
    private Long answer_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquire_id")
    private Inquire inquire;

    @Lob
    @Type(type = "text")
    @Basic(fetch = FetchType.EAGER)
    private String answerContent;

    private LocalDateTime answerTime;

}
