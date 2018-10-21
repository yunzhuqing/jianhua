package com.platform.controller;

import com.platform.entity.GoodsAttributeEntity;
import com.platform.entity.GoodsSpecificationEntity;
import com.platform.entity.ProductEntity;
import com.platform.entity.SpecificationEntity;
import com.platform.service.GoodsAttributeService;
import com.platform.service.GoodsSpecificationService;
import com.platform.service.ProductService;
import com.platform.service.SpecificationService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-30 14:31:21
 */
@RestController
@RequestMapping("product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private GoodsSpecificationService goodsSpecificationService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<ProductEntity> productList = productService.queryList(query);
        if(!CollectionUtils.isEmpty(productList)) {
            productList.forEach(productEntity -> {
                StringBuilder stringBuilder = new StringBuilder();
                String goodsSpecIds = productEntity.getGoodsSpecificationIds();
                if(StringUtils.isNotEmpty(goodsSpecIds)) {
                    String [] specArr = goodsSpecIds.split(",");
                    for(String spec : specArr) {
                        String [] specAttrs = spec.split("-");
                        if(specAttrs.length > 1) {
                            String specId = specAttrs[0];
                            String [] attrs = specAttrs[1].split("_");
                            SpecificationEntity specificationEntity = specificationService.queryObject(Integer.parseInt(specId));
                            stringBuilder.append(specificationEntity.getName() + ": ");
                            for(String attr : attrs) {
                                if(StringUtils.isNotEmpty(attr)) {
                                    GoodsSpecificationEntity goodsSpecificationEntity = goodsSpecificationService.queryObject(Integer.parseInt(attr));
                                    if(null == goodsSpecificationEntity)
                                        continue;

                                    stringBuilder.append(goodsSpecificationEntity.getValue() + " ");
                                }
                            }
                            stringBuilder.append("\n");
                        }
                    }
                }
                productEntity.setSpecificationValue(stringBuilder.toString());
            });
        }

        int total = productService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(productList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("product:info")
    public R info(@PathVariable("id") Integer id) {
        ProductEntity product = productService.queryObject(id);

        return R.ok().put("product", product);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:save")
    public R save(@RequestBody ProductEntity product) {
        productService.save(product);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:update")
    public R update(@RequestBody ProductEntity product) {
        productService.update(product);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:delete")
    public R delete(@RequestBody Integer[] ids) {
        productService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     *
     * @param params
     * @return
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<ProductEntity> list = productService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 根据goodsId查询商品
     *
     * @param goodsId
     * @return
     */
    @RequestMapping("/queryByGoodsId/{goodsId}")
    public R queryByGoodsId(@PathVariable("goodsId") String goodsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("goodsId", goodsId);
        List<ProductEntity> list = productService.queryList(params);

        return R.ok().put("list", list);
    }
}
