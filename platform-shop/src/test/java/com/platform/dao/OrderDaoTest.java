package com.platform.dao;

import com.platform.entity.OrderEntity;
import com.platform.entity.ShopEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class OrderDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private OrderDao orderDao;

    @Test
    @Rollback
    public void test() {
        int id=35;
        OrderEntity orderEntity = orderDao.queryObject(id);
        Assert.notNull(orderEntity);
        List<OrderEntity> orders = Arrays.asList(orderEntity);
        System.out.println(orders.stream().map(item -> {return item.getId();}).toArray(Integer[]::new));
    }
}