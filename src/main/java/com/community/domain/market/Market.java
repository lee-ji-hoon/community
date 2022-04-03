package com.community.domain.market;

import com.community.domain.account.Account;
import com.community.domain.board.Reply;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "marketId")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Market {

    @Id
    @GeneratedValue
    @Column(name = "market_id")
    private Long marketId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Account seller;

    private String itemStatus;

    private String itemName;

    private int price;

    private String marketType;

    @OneToMany(mappedBy = "market")
    private List<Reply> replyList = new ArrayList<>();

    @Lob
    @Type(type = "text")
    @Basic(fetch = FetchType.EAGER)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private MarketItemStatus marketItemStatus;

    @Column(columnDefinition = "TEXT")
    private String filePath;

    private LocalDateTime itemUploadTime;
}
