package com.platform.dao;

import com.platform.constants.SuggestItemType;
import com.platform.entity.SuggestEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.*;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class SuggestDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    SuggestDao suggestDao;

    @Test
    public void test() {
        int order = 1;
        SuggestEntity entity = new SuggestEntity();
        entity.setType(SuggestItemType.GOODS.ordinal());
        entity.setRid(1181001L);
        entity.setUserId(1L);
        entity.setOrder(order);
        entity.setScene(0);
        assertTrue(suggestDao.save(entity) > 0);

        SuggestEntity tmpEntity = suggestDao.queryObject(entity.getId());
        assertNotNull(tmpEntity);
    }

}