package com.platform.controller;

import com.platform.entity.AttributeEntity;
import com.platform.entity.AttributeValEntity;
import com.platform.entity.GoodsEntity;
import com.platform.service.AttributeService;
import com.platform.service.AttributeValService;
import com.platform.service.CategoryService;
import com.platform.service.GoodsService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import com.platform.vo.CategoryVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-17 16:48:17
 */
@RestController
@RequestMapping("/attribute")
public class AttributeController {
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private AttributeValService attributeValService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("attribute:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<AttributeEntity> attributeList = attributeService.queryList(query);
        int total = attributeService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(attributeList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("attribute:info")
    public R info(@PathVariable("id") Integer id) {
        AttributeEntity attribute = attributeService.queryObject(id);

        return R.ok().put("attribute", attribute);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("attribute:save")
    public R save(@RequestBody AttributeEntity attribute) {
        attributeService.save(attribute);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("attribute:update")
    public R update(@RequestBody AttributeEntity attribute) {
        attributeService.update(attribute);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("attribute:delete")
    public R delete(@RequestBody Integer[] ids) {
        attributeService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<AttributeEntity> list = attributeService.queryList(params);

        return R.ok().put("list", list);
    }


    /**
     *
     * @param attributeValEntity
     * @return
     */
    @RequestMapping("/val/save")
    public R saveVal(@RequestBody AttributeValEntity attributeValEntity) {
        if(null == attributeValEntity.getId()) {
            attributeValEntity.setCreateTime(new Date());
            attributeValEntity.setUpdateTime(new Date());
        } else {
            attributeValEntity.setUpdateTime(new Date());
        }
        attributeValService.save(attributeValEntity);
        return R.ok();
    }

    @RequestMapping("/val/list")
    public R listVal(@RequestParam Map<String, Object> params) {
        List<AttributeValEntity> list = attributeValService.queryList(params);
        return R.ok().put("list", list);
    }

    @RequestMapping("/val/info/{id}")
    public R info(@PathVariable("id") Long id) {
        AttributeValEntity attributeValEntity = attributeValService.queryObject(id);
        if(null != attributeValEntity) {
            return R.ok().put("data", attributeValEntity);
        }
        return R.error("未找到对应的属性值");
    }


    @RequestMapping("/val/delete")
    public R deleteVal(@RequestBody Integer[] ids) {
        attributeValService.deleteBatch(ids);
        return R.ok();
    }


    @RequestMapping("/val/listAttrs")
    public R listAttrs(@RequestParam("productId") Long productId) {
        R resp = R.ok();
        GoodsEntity goodsEntity = goodsService.queryObject(productId);
        Map<Integer, List<Long>> data = new HashMap<>();
        if(goodsEntity.getCategoryId() != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("attributeCategoryId", goodsEntity.getCategoryId());

            CategoryVO categoryVO = categoryService.queryObject(goodsEntity.getCategoryId());
            List<Integer> attrOptions = categoryVO.getAttrOptions();
            if(CollectionUtils.isEmpty(attrOptions)) {
                if(0 != categoryVO.getParentId()) {
                    CategoryVO parent = categoryService.queryObject(categoryVO.getParentId());
                    attrOptions = parent.getAttrOptions();
                }
            }

            if(!CollectionUtils.isEmpty(attrOptions)) {
                attrOptions.forEach(attr -> {
                    Map<String, Object> attrValParams = new HashMap<>();
                    attrValParams.put("attributeId", attr);
                    List<AttributeValEntity> attrVals = attributeValService.queryList(attrValParams);
                    data.put(attr, attrVals.stream().map(AttributeValEntity::getId).collect(Collectors.toList()));
                });
            }

        }
        resp.put("data", data);
        return resp;
    }
}
