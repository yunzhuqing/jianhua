package com.platform.service.impl;

import com.platform.dao.BaseDao;
import com.platform.dao.SceneGroupDao;
import com.platform.entity.SceneGroupEntity;
import com.platform.service.AbstractDefaultService;
import com.platform.service.SceneGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sceneGroupService")
public class SceneGroupServiceImpl extends AbstractDefaultService<SceneGroupEntity> implements SceneGroupService {

    @Autowired
    private SceneGroupDao sceneGroupDao;

    @Resource(name = "sceneGroupDao")
    @Override
    public void setBaseDao(BaseDao<SceneGroupEntity> baseDao) {
        super.setBaseDao(baseDao);
    }

    @Override
    public List<SceneGroupEntity> list(Long sceneId, Integer pageNo, Integer pageSize) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("sid", sceneId);
        attrs.put(FIELD_LIMIT, pageSize);
        attrs.put(FIELD_OFFSET, pageNo * pageSize);
        attrs.put(FIELD_SIDX, "location");
        attrs.put(FIELD_ORDER, "desc");
        return sceneGroupDao.queryList(attrs);
    }

    @Override
    public int size(Long sceneId) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("sid", sceneId);
        return sceneGroupDao.queryTotal(attrs);
    }
}
