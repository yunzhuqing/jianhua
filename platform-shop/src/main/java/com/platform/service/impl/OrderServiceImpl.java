package com.platform.service.impl;

import com.platform.constants.OrderPayStatusTypes;
import com.platform.constants.OrderStatusTypes;
import com.platform.constants.ShippingStatusTypes;
import com.platform.dao.OrderDao;
import com.platform.dao.ShippingDao;
import com.platform.entity.OrderEntity;
import com.platform.entity.ShippingEntity;
import com.platform.service.OrderService;
import com.platform.utils.RRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ShippingDao shippingDao;

    @Override
    public OrderEntity queryObject(Integer id) {
        return orderDao.queryObject(id);
    }

    @Override
    public List<OrderEntity> queryList(Map<String, Object> map) {
        return orderDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return orderDao.queryTotal(map);
    }

    @Override
    public int save(OrderEntity order) {
        return orderDao.save(order);
    }

    @Override
    public int update(OrderEntity order) {
        return orderDao.update(order);
    }

    @Override
    public int delete(Integer id) {
        return orderDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return orderDao.deleteBatch(ids);
    }

    @Override
    public int confirm(Integer id) {
        OrderEntity orderEntity = queryObject(id);
        Integer shippingStatus = orderEntity.getShippingStatus();//发货状态
        Integer payStatus = orderEntity.getPayStatus();//付款状态
        if (OrderPayStatusTypes.PAIED != payStatus) {
            throw new RRException("此订单未付款，不能确认收货！");
        }
        if (ShippingStatusTypes.REFUND == shippingStatus) {
            throw new RRException("此订单处于退货状态，不能确认收货！");
        }
        if (ShippingStatusTypes.UN_DELIVERED == shippingStatus) {
            throw new RRException("此订单未发货，不能确认收货！");
        }
        orderEntity.setShippingStatus(ShippingStatusTypes.DELIVERY_TAKEN);
        orderEntity.setOrderStatus(OrderStatusTypes.DELIVERY_TAKEN);
        return orderDao.update(orderEntity);
    }

    @Override
    public int sendGoods(OrderEntity order) {
        Integer payStatus = order.getPayStatus();//付款状态
        if (OrderPayStatusTypes.PAIED != payStatus) {
            throw new RRException("此订单未付款！");
        }

        ShippingEntity shippingEntity = shippingDao.queryObject(order.getShippingId());
        if (null != shippingEntity) {
            order.setShippingName(shippingEntity.getName());
        }
        order.setOrderStatus(OrderStatusTypes.DELIVERY);//订单已发货
        order.setShippingStatus(ShippingStatusTypes.DELIVERED);//已发货
        return orderDao.update(order);
    }
}
