package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 推荐
 */
public class SuggestEntity implements Serializable {

    private static final long serialVersionUID = 8208682972440246378L;

    private Long id;

    /**
     * 推荐物品类型
     */
    private Integer type;

    /**
     * 引用物品ID
     */
    private Long rid;

    /**
     * 排序顺序
     */
    private Integer order;

    /**
     * 创建ID
     */
    private Long userId;

    /**
     * 场景
     */
    private Integer scene;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getScene() {
        return scene;
    }

    public void setScene(Integer scene) {
        this.scene = scene;
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
