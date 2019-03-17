package com.platform.goods.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.entity.AttributeValEntity;
import com.platform.entity.GoodsAttributeEntity;
import com.platform.goods.dto.GoodsSkuEntity;
import com.platform.goods.service.GoodsSkuService;
import com.platform.goods.vo.GoodsSkuVO;
import com.platform.service.AttributeValService;
import com.platform.service.CategoryService;
import com.platform.service.GoodsAttributeService;
import com.platform.utils.R;
import com.platform.vo.CategoryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * SKU 操作接口
 */
@RestController
@RequestMapping("/sku")
public class SkuController {

    @Resource
    private GoodsSkuService goodsSkuService;

    @RequestMapping("/upsert")
    public R upsert(@RequestBody GoodsSkuVO goodsSkuVO) {
        GoodsSkuEntity goodsSkuEntity = convert(goodsSkuVO);
        if(goodsSkuVO.getId() == null) {
            goodsSkuService.save(goodsSkuEntity);
        } else {
            goodsSkuService.update(goodsSkuEntity);
        }
        return R.ok();
    }

    /**
     * 获取skud 对应的详情
     * @param id skuId
     * @return
     */
    @RequestMapping("/detail")
    public R detail(@RequestParam("id") Long id) {
        GoodsSkuVO goodsSkuVO = revert(goodsSkuService.queryObject(id));
        R  resp = R.ok();
        resp.put("data", goodsSkuVO);
        return resp;
    }


    private GoodsSkuEntity convert(GoodsSkuVO goodsSkuVO) {
        GoodsSkuEntity goodsSkuEntity = new GoodsSkuEntity();
        BeanUtils.copyProperties(goodsSkuVO, goodsSkuEntity);
        goodsSkuEntity.setAttributeIds(JSONObject.toJSONString(goodsSkuVO.getAttributeIds()));
        return goodsSkuEntity;
    }

    private GoodsSkuVO revert(GoodsSkuEntity goodsSkuEntity) {
        GoodsSkuVO goodsSkuVO = new GoodsSkuVO();
        BeanUtils.copyProperties(goodsSkuEntity, goodsSkuVO);
        goodsSkuVO.setAttributeIds(JSONObject.parseObject(goodsSkuEntity.getAttributeIds(), HashMap.class));
        return goodsSkuVO;
    }
}
