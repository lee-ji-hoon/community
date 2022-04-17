package com.community.domain.graduation;

import com.community.domain.account.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Getter
@Setter
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

    // 이미지 접근 이름
    private String imageName;

    // 이미지 주소
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

}
