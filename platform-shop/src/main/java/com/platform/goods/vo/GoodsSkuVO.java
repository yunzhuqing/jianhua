package com.platform.goods.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 商品sku
 */
public class GoodsSkuVO implements Serializable {

    private static final long serialVersionUID = -7246975771191088926L;

    private Long id;

    private Long goodsId;

    private String desc;

    private Map<Integer, Integer> attributeIds;

    private BigDecimal salePrice;

    private BigDecimal marketPrice;

    private BigDecimal purchasePrice;

    private Integer totalInventory;

    private String picUrl;

    private Boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Map<Integer, Integer> getAttributeIds() {
        return attributeIds;
    }

    public void setAttributeIds(Map<Integer, Integer> attributeIds) {
        this.attributeIds = attributeIds;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(Integer totalInventory) {
        this.totalInventory = totalInventory;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
