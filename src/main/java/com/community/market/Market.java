package com.community.market;

import com.community.account.entity.Account;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Lob
    @Type(type = "text")
    @Basic(fetch = FetchType.EAGER)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private MarketItemStatus marketItemStatus;

    private String itemImg;

    private LocalDateTime itemUploadTime;
}
