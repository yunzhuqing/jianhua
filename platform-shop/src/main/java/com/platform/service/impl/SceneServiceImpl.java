package com.platform.service.impl;

import com.platform.dao.BaseDao;
import com.platform.dao.SceneDao;
import com.platform.entity.SceneEntity;
import com.platform.service.AbstractDefaultService;
import com.platform.service.SceneService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("sceneService")
public class SceneServiceImpl extends AbstractDefaultService<SceneEntity> implements SceneService {

    @Resource(name = "sceneDao")
    @Override
    public void setBaseDao(BaseDao<SceneEntity> baseDao) {
        super.setBaseDao(baseDao);
    }

    @Override
    public List<SceneEntity> queryParent() {
        return ((SceneDao)baseDao).queryParent();
    }
}
