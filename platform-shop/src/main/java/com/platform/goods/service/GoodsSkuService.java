package com.platform.goods.service;

import com.platform.dao.BaseDao;
import com.platform.goods.dto.GoodsSkuEntity;
import com.platform.service.AbstractDefaultService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GoodsSkuService extends AbstractDefaultService<GoodsSkuEntity> {

    @Resource(name = "goodsSkuDAO")
    @Override
    public void setBaseDao(BaseDao<GoodsSkuEntity> baseDao) {
        super.setBaseDao(baseDao);
    }

}
