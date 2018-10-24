package com.platform.constants;

/**
 * 订单状态类型
 */
public class OrderStatusTypes {
    /**
     * 普通类型订单
     */
    public static final int UN_PAY = 0;

    /**
     * 订单已取消
     */
    public static final int CANCELD = 101;

    /**
     * 订单已删除
     */
    public static final int DELETED = 102;

    /**
     * 订单已付款
     */
    public static final int PAIED = 201;

    /**
     * 订单已发货
     */
    public static final int DELIVERY = 300;

    /**
     * 订单已确认收货
     */
    public static final int DELIVERY_TAKEN = 301;

    /**
     * 退款
     */
    public static final int REFUND = 401;

    /**
     * 订单完成
     */
    public static final int FINISHED = 402;

}
