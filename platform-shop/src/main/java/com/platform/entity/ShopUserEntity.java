package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺会员
 * @author yangjiaqi
 */
public class ShopUserEntity implements Serializable {

    private static final long serialVersionUID = 3190921014839157271L;

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 账号ID
     */
    private Long userId;

    /**
     * 会员店铺等级
     */
    private Long userLevel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Long userLevel) {
        this.userLevel = userLevel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
