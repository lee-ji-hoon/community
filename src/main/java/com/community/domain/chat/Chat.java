package com.community.domain.chat;

import com.community.domain.account.Account;
import com.community.domain.market.Market;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "chat_id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chat_id;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    /*private Long room;*/

    private String content;

    private LocalDateTime sendTime;

    private LocalDateTime readTime;

    private Boolean readChk;

    @ManyToOne(targetEntity = Market.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market_id;
}
