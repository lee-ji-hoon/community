package com.community.infra.aws;

import com.community.domain.board.Board;
import com.community.domain.council.Council;
import com.community.domain.graduation.Graduation;
import com.community.domain.inquire.Inquire;
import com.community.domain.market.Market;
import com.community.domain.notice.Notice;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3 {
    @Id
    @GeneratedValue
    @Column(name = "s3_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graduation_id")
    private Graduation graduation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "council_id")
    private Council council;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquire_id")
    private Inquire inquire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private String imagePath; // 이미지 접근 주소

    private String imageName; // 이미지 접근 이름
}
