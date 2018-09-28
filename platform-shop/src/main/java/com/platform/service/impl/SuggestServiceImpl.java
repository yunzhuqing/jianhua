package com.platform.service.impl;

import com.platform.dao.BaseDao;
import com.platform.entity.SuggestEntity;
import com.platform.service.AbstractDefaultService;
import com.platform.service.SuggestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("suggestService")
public class SuggestServiceImpl extends AbstractDefaultService<SuggestEntity> implements SuggestService {

    @Resource(name = "suggestDao")
    @Override
    public void setBaseDao(BaseDao<SuggestEntity> baseDao) {
        super.setBaseDao(baseDao);
    }


}
