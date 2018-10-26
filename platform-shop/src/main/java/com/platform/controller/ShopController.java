package com.platform.controller;

import com.platform.annotation.IgnoreAuth;
import com.platform.entity.AttributeCategoryEntity;
import com.platform.entity.GoodsEntity;
import com.platform.entity.ShopEntity;
import com.platform.service.ShopService;
import com.platform.utils.PageUtils;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺有关接口
 * @author yangjiaqi
 */
@RestController
@RequestMapping(value = {"/api/shops", "/shops"})
public class ShopController extends AbstractController {

    @Autowired
    private ShopService shopService;

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
        List<ShopEntity> shopList = shopService.getShops(uid, page, limit);
        int total = shopService.size(uid);
        PageUtils pageUtil = new PageUtils(shopList, total, limit, page);
        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll() {
        Long userId = getUserId();
        long uid = (null == userId) ? 0 : userId.longValue();
        List<ShopEntity> list = shopService.getShops(uid, 1, 10);
        return R.ok().put("list", list);
    }



    /**
     * 保存店铺基本信息
     * @param shop
     * @return
     */
    @RequestMapping("/save")
    public R save(@RequestBody ShopEntity shop) {
        shop.setUserId(getUserId());
        if(shopService.save(shop) > 0) {
            return R.ok();
        } else {
            return R.error("保存失败");
        }
    }

    @RequestMapping("/update")
    public R update(@RequestBody ShopEntity shop) {
        if(shopService.update(shop) > 0) {
            return R.ok();
        } else {
            return R.error("保存失败");
        }
    }

    /**
     * 查看信息
     */
    @IgnoreAuth
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ShopEntity shopEntity = shopService.queryObject(id);
        return R.ok().put("shop", shopEntity);
    }
}
