package com.community.web.dto;

import lombok.Data;

@Data
public class MarketForm {

    private String marketType;

    private String itemName;

    private int price;

    private String itemStatus;

    private String itemDetail;

    private String filePath;

}
