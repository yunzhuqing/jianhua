package com.platform.service;

import com.platform.dao.BaseDao;
import com.platform.entity.AttributeValEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AttributeValService extends AbstractDefaultService<AttributeValEntity> {
    @Resource(name = "attributeValDao")
    @Override
    public void setBaseDao(BaseDao<AttributeValEntity> baseDao) {
        super.setBaseDao(baseDao);
    }
}
