package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺属性
 *
 * @author yunzhuqing
 */
public class ShopEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6880371728500561729L;
    /*唯一标识*/
    private Integer id;
    /*店铺名称*/
    private String shopName;
    /*店铺地址*/
    private String shopAddress;
    /*用户ID*/
    private Long userId;
    /*店铺描述*/
    private String shopDesc;
    /*店铺主图*/
    private String shopPic;
    /*店铺附图*/
    private String shopSubPic;
    /*店铺状态*/
    private Integer shopState;

    /*创建时间*/
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getShopPic() {
        return shopPic;
    }

    public void setShopPic(String shopPic) {
        this.shopPic = shopPic;
    }

    public String getShopSubPic() {
        return shopSubPic;
    }

    public void setShopSubPic(String shopSubPic) {
        this.shopSubPic = shopSubPic;
    }

    public Integer getShopState() {
        return shopState;
    }

    public void setShopState(Integer shopState) {
        this.shopState = shopState;
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
