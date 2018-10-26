package com.platform.dao;

import com.platform.entity.GoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-21 21:19:49
 */
public interface GoodsDao extends BaseDao<GoodsEntity> {
    /**
     * 商品简单查询接口
     * @param map
     * @return
     */
    List<GoodsEntity> query(Map<String, Object> map);

    Integer queryMaxId();
}
