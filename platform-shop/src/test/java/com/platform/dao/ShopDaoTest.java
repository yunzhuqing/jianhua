package com.platform.dao;

import com.platform.entity.ShopEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class ShopDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ShopDao shopDao;

    @Test
    @Rollback
    public void test() {
        ShopEntity shop = new ShopEntity();
        String shopName = "shopNameTest", shopAddress = "shopAddr";
        shop.setShopName(shopName);
        shop.setShopAddress(shopAddress);
        shop.setUserId(1L);
        int code = shopDao.save(shop);
        Assert.state(code > 0, "insert failed");

        int id = shop.getId();
        ShopEntity insertShop = shopDao.queryObject(id);
        Assert.notNull(insertShop, "shop query failed");
        Assert.state(shopName.equals(insertShop.getShopName()), "shop data is not matched");
        System.out.println("time: " + insertShop.getCreateTime()+ " " + insertShop.getUpdateTime());


        String shopPic = "/shop/get.html";
        insertShop.setShopPic(shopPic);
        shopDao.update(insertShop);

        insertShop = shopDao.queryObject(id);
        Assert.state(shopPic.equals(insertShop.getShopPic()), "shop data is not matched");

        System.out.println("updateTime: " + insertShop.getUpdateTime());

        Map<String, Object> map = new HashMap<String, Object>();
        List<ShopEntity> shops = shopDao.queryList(map);
        Assert.state(shops.size() > 0, "shop query failed");
    }
}