package com.community.infra.aws;

import com.community.domain.board.Board;
import com.community.domain.council.Council;
import com.community.domain.graduation.Graduation;
import com.community.domain.market.Market;
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

    @ManyToOne
    @JoinColumn(name = "graduation_id")
    private Graduation graduation;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "council_id")
    private Council council;

    private String imagePath; // 이미지 접근 주소

    private String imageName; // 이미지 접근 이름
}
