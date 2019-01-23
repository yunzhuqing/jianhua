package com.platform.dto;


import java.io.Serializable;

public class CollectDTO implements Serializable {

    private static final long serialVersionUID = 998261700455011041L;

    private Integer typeId;

    private Integer valueId;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getValueId() {
        return valueId;
    }

    public void setValueId(Integer valueId) {
        this.valueId = valueId;
    }
}
