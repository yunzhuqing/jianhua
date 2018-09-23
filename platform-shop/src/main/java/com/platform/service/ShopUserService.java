package com.platform.service;

import com.platform.entity.ShopUserEntity;
import com.platform.vo.ShopUserVO;

import java.util.List;

/**
 * 会员接口
 */
public interface ShopUserService extends AbstractService<ShopUserEntity> {

    /**
     * 最大店铺数
     */
    static final int MAX_SHOPS = 10;

    static final String FIELD_USER_ID = "user_id";

    static final String FIELD_SHOP_ID = "shop_id";

    static final String FIELD_SHOP_ID_LIST = "shop_id_list";

    /**
     * 获取店铺会员列表
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<ShopUserVO> getUsers(Long userId, int pageNo, int pageSize);

    /**
     * 获取当前卖家的会员总数
     * @param userId
     * @return
     */
    public long size(Long userId);

    /**
     * 添加会员
     * @param shopUser
     */
    public int saveUser(ShopUserVO shopUser);

    /**
     * 更新会员信息
     * @param shopUser
     * @return
     */
    public int updateUser(ShopUserVO shopUser);

    /**
     * 删除会员
     * @param userId
     * @return
     */
    public int removeUser(Long userId);
}
