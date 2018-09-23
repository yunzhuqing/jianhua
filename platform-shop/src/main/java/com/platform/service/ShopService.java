package com.platform.service;

import com.platform.entity.ShopEntity;

import java.util.List;

/**
 * 店铺基本接口
 */
public interface ShopService extends AbstractService<ShopEntity> {

    public static final String FIELD_USER_ID = "user_id";

    /**
     * 获取当前用户下的所有店铺
     * @param userId
     * @return
     */
    public List<ShopEntity> getShops(long userId, int pageNo, int pageSize);

    /**
     * 查询某个用户下的店铺个数
     * @param userId
     * @return
     */
    public int size(long userId);
}
