package com.platform.vo;

import java.io.Serializable;

/**
 * 首页推荐物品展示信息
 */
public class SuggestItemVO implements Serializable {
    private static final long serialVersionUID = -3736547117381613437L;

    private Long id;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 引用物品ID
     */
    private Long rid;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 账号
     */
    private String userName;

    /**
     * 展示的第一层信息
     */
    private String itemFirst;

    /**
     * 展示的第二层信息
     */
    private String itemSecond;

    /**
     * 场景类别
     */
    private Integer scene;

    private Long ct;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getItemFirst() {
        return itemFirst;
    }

    public void setItemFirst(String itemFirst) {
        this.itemFirst = itemFirst;
    }

    public String getItemSecond() {
        return itemSecond;
    }

    public void setItemSecond(String itemSecond) {
        this.itemSecond = itemSecond;
    }

    public Integer getScene() {
        return scene;
    }

    public void setScene(Integer scene) {
        this.scene = scene;
    }

    public Long getCt() {
        return ct;
    }

    public void setCt(Long ct) {
        this.ct = ct;
    }
}
