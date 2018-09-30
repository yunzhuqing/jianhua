package com.platform.vo;

import java.io.Serializable;

/**
 * 场景视图
 */
public class SceneVO implements Serializable {

    private static final long serialVersionUID = 4019628846593309500L;

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
