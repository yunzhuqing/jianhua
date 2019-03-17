package com.platform.service.impl;

import com.platform.dao.CategoryDao;
import com.platform.entity.CategoryEntity;
import com.platform.service.CategoryService;
import com.platform.vo.CategoryVO;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-21 15:32:31
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public CategoryVO queryObject(Integer id) {
        return revert(categoryDao.queryObject(id));
    }

    @Override
    public List<CategoryVO> queryList(Map<String, Object> map) {
        List<CategoryEntity> categoryEntities = categoryDao.queryList(map);
        if(CollectionUtils.isEmpty(categoryEntities)) {
            return Collections.emptyList();
        }
        List<CategoryVO> categories = Lists.newArrayList();
        categoryEntities.forEach(categoryEntity -> {
            categories.add(revert(categoryEntity));
        });
        return categories;
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return categoryDao.queryTotal(map);
    }

    @Override
    public int save(CategoryVO category) {
        if ("L1".equals(category.getLevel())) {
            category.setParentId(0);
        }
        return categoryDao.save(convert(category));
    }

    @Override
    public int update(CategoryVO category) {
        if ("L1".equals(category.getLevel())) {
            category.setParentId(0);
        }
        return categoryDao.update(convert(category));
    }

    @Override
    public int delete(Integer id) {
        return categoryDao.delete(id);
    }

    @Override
    @Transactional
    public int deleteBatch(Integer[] ids) {
        categoryDao.deleteByParentBatch(ids);
        return categoryDao.deleteBatch(ids);
    }

    private CategoryVO revert(CategoryEntity categoryEntity) {
        if(null == categoryEntity)
            return null;
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setId(categoryEntity.getId());
        categoryVO.setParentId(categoryEntity.getParentId());
        categoryVO.setIconUrl(categoryEntity.getIconUrl());
        categoryVO.setFrontName(categoryEntity.getFrontName());
        categoryVO.setFrontDesc(categoryEntity.getFrontDesc());
        categoryVO.setBannerUrl(categoryEntity.getBannerUrl());
        String attributeOptions = categoryEntity.getAttrOptions();
        List<Integer> attributes = Lists.newArrayList();
        if(!StringUtils.isEmpty(attributeOptions)) {
            String [] attrs = attributeOptions.split(",");
            for(String attr : attrs) {
                if(!"".equals(attr)) {
                    attributes.add(Integer.parseInt(attr));
                }
            }
        }
        categoryVO.setAttrOptions(attributes);
        categoryVO.setImgUrl(categoryEntity.getImgUrl());
        categoryVO.setKeywords(categoryEntity.getKeywords());
        categoryVO.setLevel(categoryEntity.getLevel());
        categoryVO.setName(categoryEntity.getName());
        categoryVO.setIsShow(categoryEntity.getIsShow());
        categoryVO.setType(categoryEntity.getType());
        categoryVO.setWapBannerUrl(categoryEntity.getWapBannerUrl());
        return categoryVO;
    }


    private CategoryEntity convert(CategoryVO category) {
        if(null == category)
            return null;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setBannerUrl(category.getBannerUrl());
        categoryEntity.setFrontDesc(category.getFrontDesc());
        categoryEntity.setFrontName(category.getFrontName());
        categoryEntity.setIconUrl(category.getIconUrl());
        categoryEntity.setId(category.getId());
        categoryEntity.setImgUrl(category.getImgUrl());
        categoryEntity.setIsShow(category.getIsShow());
        categoryEntity.setKeywords(category.getKeywords());
        categoryEntity.setLevel(category.getLevel());
        categoryEntity.setShow(category.getShow());
        categoryEntity.setShowIndex(category.getShowIndex());
        categoryEntity.setSortOrder(category.getSortOrder());
        categoryEntity.setType(category.getType());
        categoryEntity.setWapBannerUrl(category.getWapBannerUrl());
        categoryEntity.setParentId(category.getParentId());
        categoryEntity.setName(category.getName());
        categoryEntity.setKeywords(category.getKeywords());
        List<Integer> attrOptions = category.getAttrOptions();
        if(null != attrOptions) {
            String options = "";
            for(Integer attrId : attrOptions) {
                options = options + "," + attrId;
            }
            categoryEntity.setAttrOptions(options);
        }
        return categoryEntity;
    }
}
