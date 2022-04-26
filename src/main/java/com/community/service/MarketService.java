package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.market.Market;
import com.community.domain.market.MarketItemStatus;
import com.community.domain.market.MarketRepository;
import com.community.infra.aws.S3;
import com.community.infra.aws.S3Repository;
import com.community.infra.aws.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MarketService {

    private final MarketRepository marketRepository;
    private final S3Repository s3Repository;

    private final S3Service s3Service;


    public Market createNewItem(List<MultipartFile> multipartFileLIst, String itemName, String marketType,
                                String description, int price,
                                String itemStatus, Account account) {
        Market market = Market.builder()
                .marketType(marketType)
                .itemName(itemName)
                .itemDetail(description)
                .price(price)
                .itemStatus(itemStatus)
                .seller(account)
                .uploadTime(LocalDateTime.now())
                .build();

        marketSetType(market, market.getMarketType());

        uploadImage(multipartFileLIst, market);

        return marketRepository.save(market);
    }

    public void uploadImage(List<MultipartFile> multipartFiles, Market market) {
        String uploadFolder = "market-img/";

        if (multipartFiles != null) {
            List<String> imageFileList = s3Service.upload(multipartFiles, uploadFolder);

            for (String imageFileName : imageFileList) {
                S3 s3 = new S3();
                s3.setImageName(uploadFolder + imageFileName);
                s3.setImagePath(S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + uploadFolder + imageFileName);
                s3.setMarket(market);

                s3Repository.save(s3);
            }
        }
    }

    private void marketSetType(Market market, String marketType) {
        switch (marketType) {
            case "sell" :
                market.setMarketItemStatus(MarketItemStatus.SELLING);
                break;
            case "buy":
                market.setMarketItemStatus(MarketItemStatus.PURCHASE);
                break;
            case "share":
                market.setMarketItemStatus(MarketItemStatus.SHARE);
                break;
        }
    }

    public void updateMarketItemType(Market market, String marketType) {
        log.info("marketType : {}", marketType);
        switch (marketType){
            case "selling" :
                market.setMarketItemStatus(MarketItemStatus.SELLING);
                log.info("marketType 판매 중으로 변경");
                break;
            case "sold-out":
                market.setMarketItemStatus(MarketItemStatus.SOLDOUT);
                market.setPublished(false);
                log.info("marketType 판매 완료 변경");
                break;
            case "purchase" :
                market.setMarketItemStatus(MarketItemStatus.PURCHASE);
                log.info("marketType 구매중으로 변경");
                break;
            case "purchase-end":
                market.setMarketItemStatus(MarketItemStatus.COMPLETEPURCHASE);
                market.setPublished(false);
                log.info("marketType 구매 완료 변경");
                break;
            case "share" :
                market.setMarketItemStatus(MarketItemStatus.SHARE);
                log.info("marketType 나눔 중으로 변경");
                break;
            case "share-end":
                market.setMarketItemStatus(MarketItemStatus.COMPLETESHARE);
                market.setPublished(false);
                log.info("marketType 나눔 완료 변경");
                break;
        }
        marketRepository.save(market);

    }

    public void deleteProduct(Market market) {
        List<S3> imageList = market.getImageList();

        for (S3 s3 : imageList) s3Service.deleteFile(s3.getImageName());

        marketRepository.delete(market);
    }

    public void updateMarket(Market market, List<MultipartFile> multipartFileLIst, String itemName,
                             String marketType, String description, String price, String itemStatus) {
        market.setItemName(itemName);
        market.setMarketType(marketType);
        market.setItemDetail(description);
        market.setPrice(Integer.parseInt(price));
        market.setItemStatus(itemStatus);
        marketSetType(market, market.getMarketType());

        uploadImage(multipartFileLIst, market);
    }

    /*public void updateMarketImage(Market market, String marketImagePath, String uploadFile, String uploadFolder) {
        market.setFilePath(marketImagePath);
        market.setFileName(uploadFolder + uploadFile);

        marketRepository.save(market);
    }

    public void updateMarket(MarketForm marketForm, Market market, Account account, String marketType) {
        marketSetType(market, marketType);

        market.setPrice(marketForm.getPrice());
        market.setItemDetail(marketForm.getItemDetail());
        market.setItemName(marketForm.getItemName());
        market.setItemStatus(marketForm.getItemStatus());
        market.setMarketType(marketForm.getMarketType());

        marketRepository.save(market);
    }*/


}
