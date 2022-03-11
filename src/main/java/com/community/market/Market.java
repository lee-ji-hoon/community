package com.community.market;

import com.community.account.entity.Account;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Market {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "seller_id")
    private Account account;

    private String itemName;

    private int price;

    private String type;

    private String itemDetail;

    private MarketItemStatus marketItemStatus;
}
