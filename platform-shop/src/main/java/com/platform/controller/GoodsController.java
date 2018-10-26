package com.platform.controller;

import com.platform.annotation.IgnoreAuth;
import com.platform.entity.GoodsEntity;
import com.platform.entity.GoodsVo;
import com.platform.entity.RelatedGoodsVo;
import com.platform.service.GoodsService;
import com.platform.service.RelatedGoodsService;
import com.platform.util.ApiBaseAction;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-21 21:19:49
 */
@RestController
@RequestMapping(value = {"/api/goods", "/goods"})
public class GoodsController extends ApiBaseAction {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RelatedGoodsService relatedGoodsService;

    @IgnoreAuth
    @RequestMapping("/listGoods")
    public R listGoods(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        query.put("isDelete", 0);
        List<GoodsEntity> goodsList = goodsService.query(params);
        int total = goodsService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(goodsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("goods:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        query.put("isDelete", 0);
        List<GoodsEntity> goodsList = goodsService.queryList(query);
        int total = goodsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("goods:info")
    public R info(@PathVariable("id") Long id) {
        GoodsEntity goods = goodsService.queryObject(id);

        return R.ok().put("goods", goods);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("goods:save")
    public R save(@RequestBody GoodsEntity goods) {
        goodsService.save(goods);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("goods:update")
    public R update(@RequestBody GoodsEntity goods) {
        goodsService.update(goods);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("goods:delete")
    public R delete(@RequestBody Long[] ids) {
        goodsService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        params.put("isDelete", 0);
        List<GoodsEntity> list = goodsService.queryList(params);

        return R.ok().put("list", list);
    }


    /**
     * 商品回收站
     *
     * @param params
     * @return
     */
    @RequestMapping("/historyList")
    public R historyList(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        query.put("isDelete", 1);
        List<GoodsEntity> goodsList = goodsService.queryList(query);
        int total = goodsService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(goodsList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 商品从回收站恢复
     */
    @RequestMapping("/back")
    @RequiresPermissions("goods:back")
    public R back(@RequestBody Long[] ids) {
        goodsService.back(ids);

        return R.ok();
    }

    /**
     * 总计
     */
    @RequestMapping("/queryTotal")
    public R queryTotal(@RequestParam Map<String, Object> params) {

        params.put("isDelete", 0);
        int sum = goodsService.queryTotal(params);
        return R.ok().put("goodsSum", sum);
    }

    /**
     * 上架
     */
    @RequestMapping("/enSale")
    public R enSale(@RequestBody Long id) {
        goodsService.enSale(id);

        return R.ok();
    }

    /**
     * 下架
     */
    @RequestMapping("/unSale")
    public R unSale(@RequestBody Long id) {
        goodsService.unSale(id);

        return R.ok();
    }

    @ApiOperation(value = "商品详情页")
    @IgnoreAuth
    @GetMapping(value = "related")
    public Map<String, Object> related(Long id) {
        Map<String, Object> resultObj = new HashMap();
        Map param = new HashMap();
        param.put("goods_id", id);
        param.put("fields", "related_goods_id");
        List<RelatedGoodsVo> relatedGoodsEntityList = relatedGoodsService.queryList(param);

        List<Integer> relatedGoodsIds = new ArrayList();
        for (RelatedGoodsVo relatedGoodsEntity : relatedGoodsEntityList) {
            relatedGoodsIds.add(relatedGoodsEntity.getRelated_goods_id());
        }

        List<GoodsVo> relatedGoods = new ArrayList<GoodsVo>();

        if (null == relatedGoodsIds || relatedGoods.size() < 1) {
            //查找同分类下的商品
            GoodsEntity goodsCategory = goodsService.queryObject(id);
            Map paramRelated = new HashMap();
            paramRelated.put("fields", "id, name, list_pic_url, retail_price");
            paramRelated.put("category_id", goodsCategory.getCategoryId());
            relatedGoods = goodsService.queryList(paramRelated);
        } else {
            Map paramRelated = new HashMap();
            paramRelated.put("goods_ids", relatedGoodsIds);
            paramRelated.put("fields", "id, name, list_pic_url, retail_price");
            relatedGoods = goodsService.queryList(paramRelated);
        }
        resultObj.put("goodsList", relatedGoods);
        return toResponsSuccess(resultObj);
    }
}
