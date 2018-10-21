package com.platform.service;

import com.platform.dao.ApiProductMapper;
import com.platform.entity.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ApiProductService {
    @Autowired
    private ApiProductMapper productDao;


    public ProductVo queryObject(Integer id) {
        return productDao.queryObject(id);
    }


    public List<ProductVo> queryList(Map<String, Object> map) {
        return productDao.queryList(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return productDao.queryTotal(map);
    }


    public void save(ProductVo goods) {
        productDao.save(goods);
    }


    public void update(ProductVo goods) {
        productDao.update(goods);
    }


    public void delete(Integer id) {
        productDao.delete(id);
    }


    public void deleteBatch(Integer[] ids) {
        productDao.deleteBatch(ids);
    }


    /**
     * 将库存数量减一
     * @param id product 唯一标识
     * @param goodsNumber 原始库存数量
     * @param num
     * @return
     */
    public int decreaseGoodsNumber(Long id, Integer goodsNumber, Integer num) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("goodsNumber", goodsNumber);
        params.put("num", num);
        return productDao.decreaseGoodsNumber(params);
    }

}
