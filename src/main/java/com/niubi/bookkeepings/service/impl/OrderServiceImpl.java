package com.niubi.bookkeepings.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.niubi.bookkeepings.Excetion.DeleteExcetion;
import com.niubi.bookkeepings.domain.dto.orderDto;
import com.niubi.bookkeepings.domain.dto.orderPageDto;
import com.niubi.bookkeepings.domain.po.Bag;
import com.niubi.bookkeepings.domain.po.Order;
import com.niubi.bookkeepings.domain.po.OrderDetail;
import com.niubi.bookkeepings.domain.vo.OrderDetailVo;
import com.niubi.bookkeepings.domain.vo.OrderVos;
import com.niubi.bookkeepings.domain.vo.orderPageVo;
import com.niubi.bookkeepings.domain.vo.orderVo;
import com.niubi.bookkeepings.mapper.OrderMapper;
import com.niubi.bookkeepings.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    private final IOrderDetailService orderDetailService;
    private final IEmployeeService employeeService;
    private final IProcessService processService;
    private final IBagService bagService;


    @Override
    @Transactional
    public void addOrder(orderDto order) {
        Order order1 = BeanUtil.copyProperties(order, Order.class);
        save(order1);
        Integer id = order1.getId();
        List<OrderDetail> orderDetailList = order.getOrderDetailList();
        for (OrderDetail detail : orderDetailList) {
            detail.setOderId(id);
        }
        orderDetailService.saveBatch(orderDetailList);
    }

    @Override
    public orderPageVo pageOrder(orderPageDto orderPage) {
        LocalDate startTime = orderPage.getStartTime();
        LocalDate endTime = orderPage.getEndTime();

        orderPageVo vo=new orderPageVo();
        Page<Order> page=new Page<>();
        List<Bag> bagList=null;
        List<Integer> bagIds=null;
        if(orderPage.getBagName()!=null && !Objects.equals(orderPage.getBagName(), "undefined")) {
            bagList = bagService.lambdaQuery()
                    .like(Bag::getName, orderPage.getBagName()).list();
            bagIds = bagList.stream().map(Bag::getId).collect(Collectors.toList());
        }

        if(startTime!=null && endTime!=null){
           page = lambdaQuery().like(orderPage.getName()!=null,Order::getName, orderPage.getName())
                   .in(bagList!=null,Order::getBagId,bagIds)
                   .ge(Order::getTime, startTime)
                   .le(Order::getTime, endTime)
                   .page(new Page<>(orderPage.getPageNo(), orderPage.getPageSize()));
       }
       else{
           page = lambdaQuery().like(orderPage.getName()!=null && !Objects.equals(orderPage.getName(), "undefined"),Order::getName, orderPage.getName())
                   .in(bagList!=null,Order::getBagId,bagIds)
                   .page(new Page<>(orderPage.getPageNo(), orderPage.getPageSize()));
       }
        List<Order> records = page.getRecords();
       List<OrderVos> recordvos=new ArrayList<>();
        for (Order record : records) {
            OrderVos orderVos = BeanUtil.copyProperties(record, OrderVos.class);
            orderVos.setTimes(YearMonth.of(record.getTime().getYear(), record.getTime().getMonth()));
            orderVos.setBagName(bagService.getBagById(record.getBagId()).getName());
            orderVos.setImageUrl(bagService.getBagById(record.getBagId()).getImageUrl());
            recordvos.add(orderVos);
        }
        vo.setOrderList(recordvos);
       vo.setTotalPage(page.getPages());
       vo.setTotalData(page.getTotal());
        return vo;
    }

    @Override
    public orderVo getOrderById(Integer orderId) {
        orderVo vo=new orderVo();
        Order order = getById(orderId);
        List<OrderDetailVo> listvo=new ArrayList<>();
        if(order==null){
            throw new DeleteExcetion("订单不存在");
        }
        vo.setName(order.getName());
        LocalDate time = order.getTime();
        int year = time.getYear();
        Month month = time.getMonth();
        YearMonth yearMonth = YearMonth.of(year, month);
        vo.setTime(yearMonth);
        vo.setBagName(bagService.getBagById(order.getBagId()).getName());
        vo.setOderId(order.getId());
        vo.setImageUrl(bagService.getBagById(order.getBagId()).getImageUrl());
        List<OrderDetail> list = orderDetailService.lambdaQuery()
                .eq(OrderDetail::getOderId, orderId).list();
        for (OrderDetail detail : list) {
            OrderDetailVo detailVo = new OrderDetailVo();
            detailVo.setId(detail.getId());
            detailVo.setOderId(detail.getOderId());
            detailVo.setEmployeeName(employeeService.getById(detail.getEmployeeId()).getName());
            detailVo.setProcessName(processService.getById(detail.getProcessId()).getName());
            detailVo.setRealPrice(detail.getRealPrice());
            detailVo.setRealQuantity(detail.getRealQuantity());
            detailVo.setEmployeeId(detail.getEmployeeId());
            detailVo.setProcessId(detail.getProcessId());
            listvo.add(detailVo);
        }
        log.info("listvo:"+listvo);
        vo.setOrderDetailVoList(listvo);
        return vo;
    }

    @Override
    @Transactional
    public void deleteOrder(Integer orderId) {
        removeById(orderId);
        orderDetailService.lambdaUpdate()
                .eq(OrderDetail::getOderId, orderId)
                .remove();
    }

    @Override
    @Transactional
    public void updateOrder(orderDto order) {
        Integer orderId = order.getOderId();
        lambdaUpdate()
                .eq(Order::getId, orderId)
                .set(order.getName()!=null,Order::getName, order.getName())
                .set(order.getTime()!=null,Order::getTime, order.getTime())
                .set(order.getBagId()!=null,Order::getBagId, order.getBagId())
                .update();
        orderDetailService.lambdaUpdate()
                .eq(OrderDetail::getOderId, orderId)
                .remove();
        List<OrderDetail> orderDetailList = order.getOrderDetailList();
        for (OrderDetail detail : orderDetailList) {
            if(detail.getEmployeeId()==null){
                throw new DeleteExcetion("员工不存在");
            }
            if(detail.getProcessId()==null){
                throw new DeleteExcetion("工序不存在");
            }
            detail.setOderId(orderId);
        }
        orderDetailService.saveBatch(orderDetailList);

    }

}
