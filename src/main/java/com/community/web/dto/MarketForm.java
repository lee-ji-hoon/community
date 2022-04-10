package com.community.web.dto;

import com.community.domain.market.Market;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MarketForm {

    private String marketType;

    private String itemName;

    private int price;

    private String itemStatus;

    private String itemDetail;

    private String filePath;

    public MarketForm(Market market) {
        this.marketType = market.getMarketType();
        this.itemName = market.getItemName();
        this.price = market.getPrice();
        this.itemStatus = market.getItemStatus();
        this.itemDetail = market.getItemDetail();
        this.filePath = market.getFilePath();
    }

}

