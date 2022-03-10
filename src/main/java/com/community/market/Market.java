package com.community.market;

import com.community.account.entity.Account;
import com.community.study.Study;
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

    private String itemDetail;

    private MarketItemStatus marketItemStatus;




}
