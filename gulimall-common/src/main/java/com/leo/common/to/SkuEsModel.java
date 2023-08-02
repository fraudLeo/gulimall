package com.leo.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuEsModel {
    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    //销量
    private Long saleCount;

    //是否有库存
    private Boolean hasStock;

    //热度
    private Long hotScore;

    private Long brandId;

    private Long catalogId;

    //不在数据库中存储,是一个冗余字段
    private String brandName;

    private String brandImg;

    private String catelogName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private Long skuId;

        private String attrName;

        private String attrValue;
    }

}
