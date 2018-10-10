package com.platform.dao;

import com.platform.constants.SuggestItemType;
import com.platform.entity.GoodsVo;
import com.platform.entity.SuggestEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class ApiGoodsDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    ApiGoodsMapper apiGoodsMapper;

    @Test
    public void test() {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("name", "1");
        List<GoodsVo> goods = apiGoodsMapper.queryList(attrs);
        assertTrue(goods.size() > 0);
    }

}