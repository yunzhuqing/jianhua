package com.platform.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 场景视图
 */
public class SceneInnerVO extends SceneVO {

    private static final long serialVersionUID = -5505313893095755987L;

    /**
     * 创建人
     */
    private String userName;

    /**
     * 创建时间
     */
    private Date ct;

    private Long parentId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCt() {
        return ct;
    }

    public void setCt(Date ct) {
        this.ct = ct;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
