package com.platform.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 场景视图
 */
public class SceneGroupInnerVO implements Serializable {

    private static final long serialVersionUID = -5505313893095755987L;

    private Long id;

    private Long sid;

    /**
     * scene 0-商品 1-活动
     */
    private Integer type;

    /**
     * 引用ID 商品ID或者活动ID
     */
    private Long rid;

    /**
     * 商品名称、活动名称
     */
    private String name;

    private Long userId;

    private String pictureUrl;

    /**
     * 系统
     */
    private Integer from;

    /**
     * 商品顺序
     */
    private Integer location;

    private Date createTime;

    private Date updateTime;

    /**
     * 用户姓名
     */
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
