package com.community.domain.market;

import com.community.domain.account.Account;
import com.community.domain.reply.Reply;
import com.community.domain.bookmark.Bookmark;
import com.community.infra.aws.S3;
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

    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3> imageList = new ArrayList<>();

    @Lob
    @Type(type = "text")
    @Basic(fetch = FetchType.EAGER)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private MarketItemStatus marketItemStatus;

    private LocalDateTime uploadTime;

    private boolean published = true;
}
