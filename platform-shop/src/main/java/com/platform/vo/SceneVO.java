package com.platform.vo;

import com.platform.entity.Tree;

import java.io.Serializable;
import java.util.List;

/**
 * 场景视图
 */
public class SceneVO implements Serializable {

    private static final long serialVersionUID = 4019628846593309500L;

    private Long id;

    private String name;

    private List<SuggestItemVO> suggestItems;

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

    public List<SuggestItemVO> getSuggestItems() {
        return suggestItems;
    }

    public void setSuggestItems(List<SuggestItemVO> suggestItems) {
        this.suggestItems = suggestItems;
    }
}
