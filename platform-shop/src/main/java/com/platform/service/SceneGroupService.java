package com.platform.service;

import com.platform.entity.SceneEntity;
import com.platform.entity.SceneGroupEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 场景接口
 */
public interface SceneGroupService extends AbstractService<SceneGroupEntity> {
    /**
     * 根据场景ID 获取场景聚类
     * @param sceneId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<SceneGroupEntity> list(Long sceneId, Integer pageNo, Integer pageSize);

    /**
     * 返回场景ID 记录个数
     * @param sceneId
     * @return
     */
    public int size(Long sceneId);
}
