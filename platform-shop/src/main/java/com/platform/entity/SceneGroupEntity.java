package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 场景定制
 */
public class SceneGroupEntity implements Serializable {

    private static final long serialVersionUID = -5796085203396138520L;

    private Long id;

    /**
     * 场景ID
     */
    private Long sid;

    /**
     * 场景类型
     * 0-商品 1-活动
     */
    private Integer type;

    /**
     * 引用ID
     */
    private Long rid;

    /**
     * 场景ID
     */
    private Long userId;

    /**
     * 创建类型 0-系统 1-用户自定义
     */
    private Integer from;

    private Date createTime;

    private Date updateTime;

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

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
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
