package com.platform.dao;

import com.platform.constants.SuggestItemType;
import com.platform.entity.SceneEntity;
import com.platform.entity.SuggestEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class SceneDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    SceneDao sceneDao;

    @Test
    @Rollback
    public void test() {
        int order = 1;
        String name = "测试场景_" + System.currentTimeMillis();
        SceneEntity sceneEntity = new SceneEntity();

        sceneEntity.setName(name);
        assertTrue(sceneDao.save(sceneEntity) > 0);
        assertTrue(sceneEntity.getId() > 0);

        SceneEntity tmpEntity = sceneDao.queryObject(sceneEntity.getId());
        assertNotNull(tmpEntity);
        assertTrue(name.equals(tmpEntity.getName()));

        assertTrue(sceneDao.delete(sceneEntity.getId()) > 0);
    }

}