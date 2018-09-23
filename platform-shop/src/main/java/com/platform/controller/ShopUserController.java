package com.platform.controller;

import com.platform.entity.ShopEntity;
import com.platform.entity.ShopUserEntity;
import com.platform.service.ShopService;
import com.platform.service.ShopUserService;
import com.platform.utils.PageUtils;
import com.platform.utils.R;
import com.platform.vo.ShopUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 店铺有关接口
 * @author yangjiaqi
 */
@RestController
@RequestMapping("shop/user")
public class ShopUserController extends AbstractController {

    @Autowired
    private ShopUserService shopUserService;

    /**
     * 获取当前用户的所有店铺列表
     * @return
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        int page = Integer.parseInt(params.get("page").toString());
        int limit = Integer.parseInt(params.get("limit").toString());
        Long userId = getUserId();
        long uid = (null == userId) ? 0 : userId.longValue();
        List<ShopUserVO> shopList = shopUserService.getUsers(uid, page, limit);
        int total = (int)shopUserService.size(userId);
        PageUtils pageUtil = new PageUtils(shopList, total, limit, page);
        return R.ok().put("page", pageUtil);
    }


    /**
     * 保存会员信息
     * @param shopUserVO
     * @return
     */
    @RequestMapping("/save")
    public R save(@RequestBody ShopUserVO shopUserVO) {
        shopUserVO.setCreateUserId(getUserId());
        shopUserService.saveUser(shopUserVO);
        return R.ok();
    }

    /**
     * 更新会员
     * @param shopUserVO
     * @return
     */
    @RequestMapping("/update")
    public R update(@RequestBody ShopUserVO shopUserVO) {
        return R.ok();
    }


}
