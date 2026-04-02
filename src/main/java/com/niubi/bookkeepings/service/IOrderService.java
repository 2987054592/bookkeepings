package com.niubi.bookkeepings.service;

import com.niubi.bookkeepings.domain.dto.orderDto;
import com.niubi.bookkeepings.domain.dto.orderPageDto;
import com.niubi.bookkeepings.domain.po.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.niubi.bookkeepings.domain.vo.orderPageVo;
import com.niubi.bookkeepings.domain.vo.orderVo;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
public interface IOrderService extends IService<Order> {

    void addOrder(orderDto order);

    orderPageVo pageOrder(orderPageDto orderPage);

    orderVo getOrderById(Integer orderId);

    void deleteOrder(Integer orderId);

    void updateOrder(orderDto order);
}
