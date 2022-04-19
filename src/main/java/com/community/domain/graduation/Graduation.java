package com.community.domain.graduation;

import com.community.domain.account.Account;
import com.community.infra.aws.S3;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Graduation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "graduation_id ")
    private Long id;

    private String title;

    private String teamName;

    private String description;

    private String path;

    private String graduationType;

    private String teamMember;

    private LocalDateTime uploadTime;

    private int graduationDate;

    @OneToMany(mappedBy = "graduation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3> imageList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public void addImage(S3 s3, Graduation graduation) {
        log.info("s3 : {}", s3.getImageName());
        log.info("graduation : {}", graduation);
        graduation.imageList.add(s3);
    }

}
