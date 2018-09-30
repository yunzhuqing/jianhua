package com.platform.dao;

import com.platform.constants.SuggestItemType;
import com.platform.entity.SceneEntity;
import com.platform.entity.SceneGroupEntity;
import com.platform.entity.SuggestEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class SceneGroupDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    SceneDao sceneDao;

    @Autowired
    SceneGroupDao sceneGroupDao;


    @Test
    @Rollback
    public void test() {
        int order = 1;
        String name = "测试场景_" + System.currentTimeMillis();
        SceneEntity sceneEntity = new SceneEntity();

        sceneEntity.setName(name);
        assertTrue(sceneDao.save(sceneEntity) > 0);
        Long sceneId = sceneEntity.getId() ;

        SceneGroupEntity sceneGroupEntity = new SceneGroupEntity();
        sceneGroupEntity.setSid(sceneId);
        sceneGroupEntity.setType(1);
        sceneGroupEntity.setFrom(1);

        sceneGroupDao.save(sceneGroupEntity);
        assertTrue(sceneGroupEntity.getId() > 0);
        SceneGroupEntity groupEntity = sceneGroupDao.queryObject(sceneGroupEntity.getId());
        assertNotNull(groupEntity);

        assertTrue(1 == groupEntity.getType());

        assertTrue(sceneGroupDao.delete(groupEntity.getId()) > 0);
    }

}