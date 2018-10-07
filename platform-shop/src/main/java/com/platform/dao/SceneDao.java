package com.platform.dao;

import com.platform.entity.SceneEntity;

import java.util.List;

/**
 * 场景接口
 */
public interface SceneDao extends BaseDao<SceneEntity> {

    /**
     * 获取所有的父节点
     * @return
     */
    public List<SceneEntity> queryParent();
}
