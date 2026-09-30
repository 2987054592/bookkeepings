package com.niubi.bookkeepings.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.niubi.bookkeepings.Excetion.DeleteExcetion;
import com.niubi.bookkeepings.domain.dto.SalaryAggregateDto;
import com.niubi.bookkeepings.domain.dto.employeePageDto;
import com.niubi.bookkeepings.domain.po.Bag;
import com.niubi.bookkeepings.domain.po.Employee;
import com.niubi.bookkeepings.domain.po.Order;
import com.niubi.bookkeepings.domain.po.OrderDetail;
import com.niubi.bookkeepings.domain.po.Process;
import com.niubi.bookkeepings.domain.vo.*;
import com.niubi.bookkeepings.mapper.EmployeeMapper;
import com.niubi.bookkeepings.mapper.OrderDetailMapper;
import com.niubi.bookkeepings.mapper.OrderMapper;
import com.niubi.bookkeepings.mapper.ProcessMapper;
import com.niubi.bookkeepings.service.IBagService;
import com.niubi.bookkeepings.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niubi.bookkeepings.service.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final IOrderDetailService orderDetailService;
    private final IBagService bagService;
    private final ProcessMapper processMapper;
    @Override
    public employeePageVo pageemployee(employeePageDto employeePage) {
        employeePageVo employeePageVo = new employeePageVo();
        String name = employeePage.getName();
        Integer floor = employeePage.getFloor();
        Integer pageSize = employeePage.getPageSize();
        Integer pageNo = employeePage.getPageNo();
        Page<Employee> page = lambdaQuery()
                .like(name != null && !name.isEmpty(), Employee::getName, name)
                .eq(floor != null, Employee::getFloor, floor)
                .orderByDesc(Employee::getCreateTime)
                .orderByDesc(Employee::getId)
                .page(new Page<>(pageNo, pageSize));
        List<Employee> records = page.getRecords();
        if(records.isEmpty()){
            employeePageVo.setTotalData(0);
            employeePageVo.setTotalPage(0);
            employeePageVo.setEmployeeVoList(new ArrayList<>());
            return employeePageVo;
        }
        List<employeeVo> emvo= new ArrayList<>();
        List<Integer> employeeIds = records.stream().map(Employee::getId).collect(Collectors.toList());
        //key是员工id，value是员工id对应的的薪水
        Map<Integer, List<employeeMonthSalary>> salarymap = getSalaryById(employeeIds);
        for (Employee record : records) {
            employeeVo employeeVo = BeanUtil.copyProperties(record, employeeVo.class);
            employeeVo.setSalary(salarymap.get(record.getId()));
            emvo.add(employeeVo);
        }
        employeePageVo.setEmployeeVoList(emvo);
        employeePageVo.setTotalData(page.getTotal());
        employeePageVo.setTotalPage(page.getPages());
        return employeePageVo;
    }

    ///根据员工id，根据时间进行分类获得对应的薪水（聚合 SQL 一次查出，不再拉全量明细）
    public Map<Integer,List<employeeMonthSalary>> getSalaryById(List<Integer> employeeId) {
        Map<Integer,List<employeeMonthSalary>> vo = new HashMap<>();
        for(Integer id:employeeId){
            vo.put(id, new ArrayList<>());
        }
        if (employeeId == null || employeeId.isEmpty()) {
            return vo;
        }
        //按 员工+月份 聚合，一次查询得到所有员工的月薪
        List<SalaryAggregateDto> aggs = orderDetailMapper.selectSalaryGroupByMonth(employeeId);
        for (SalaryAggregateDto agg : aggs) {
            YearMonth month = YearMonth.parse(agg.getMonth());
            employeeMonthSalary salary = new employeeMonthSalary(month, agg.getSalary());
            List<employeeMonthSalary> list = vo.get(agg.getEmployeeId());
            if (list != null) {
                list.add(salary);
            }
        }
        return vo;
    }

    //详情一次最多返回的订单数
    private static final int DETAIL_ORDER_LIMIT = 50;

    //查询这个员工薪水的详情（一次查最多50个订单，前端本地分页；子查询 + 主表回查）
    @Override
    public employeeMonthSalaryPageVo employeegetById(Integer employeeId) {
        //先确定结果vo
        employeeMonthSalaryPageVo pageVo = new employeeMonthSalaryPageVo();
        List<employeeMonthSalaryVo> employeeMonthSalaryVo = new ArrayList<>();
        pageVo.setOrderList(employeeMonthSalaryVo);
        //总数与工资总额、按月聚合（全量，一次聚合查询）
        Long total = orderDetailMapper.countDistinctOrdersByEmployee(employeeId);
        pageVo.setTotalData(total == null ? 0 : total);
        pageVo.setTotalSalary(orderDetailMapper.sumSalaryByEmployee(employeeId));
        List<employeeMonthSalary> monthlyList = new ArrayList<>();
        for (SalaryAggregateDto agg : orderDetailMapper.selectSalaryGroupByMonth(Collections.singletonList(employeeId))) {
            monthlyList.add(new employeeMonthSalary(YearMonth.parse(agg.getMonth()), agg.getSalary()));
        }
        pageVo.setMonthlySalaryList(monthlyList);
        if (total == null || total == 0) {
            return pageVo;
        }
        //第一步：子查询取最近 50 个订单id（按订单创建时间倒序）
        List<Integer> orderIds = orderDetailMapper.selectOrderIdsByEmployeePaged(employeeId, 0, DETAIL_ORDER_LIMIT);
        if (orderIds.isEmpty()) {
            return pageVo;
        }
        //第二步：用这批订单id回查订单主表，并保持分页顺序
        List<Order> orders = orderMapper.selectByIds(orderIds);
        Map<Integer, Order> orderMap = orders.stream()
                .collect(Collectors.toMap(Order::getId, o -> o));
        //第三步：只查当前页订单下、该员工自己的明细（范围已缩小到一页）
        List<OrderDetail> orderDetailList = orderDetailService.lambdaQuery()
                .eq(OrderDetail::getEmployeeId, employeeId)
                .in(OrderDetail::getOderId, orderIds)
                .list();
        Map<Integer, List<OrderDetail>> detailsByOrder = orderDetailList.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOderId));
        //批量取书包与工序，避免 N+1 查询
        List<Integer> bagIds = orders.stream().map(Order::getBagId).distinct().collect(Collectors.toList());
        Map<Integer, Bag> bagMap = bagService.listByIds(bagIds).stream()
                .collect(Collectors.toMap(Bag::getId, b -> b));
        List<Integer> processIds = orderDetailList.stream()
                .map(OrderDetail::getProcessId).distinct().collect(Collectors.toList());
        Map<Integer, Process> processMap = processIds.isEmpty() ? Collections.emptyMap()
                : processMapper.selectBatchIds(processIds).stream()
                        .collect(Collectors.toMap(Process::getId, p -> p));
        String employeeName = getById(employeeId).getName();
        //按分页顺序组装
        for (Integer orderId : orderIds) {
            Order order = orderMap.get(orderId);
            if (order == null) {
                continue;
            }
            //组装employeeMonthSalaryVo
            employeeMonthSalaryVo employeeMonthSalarVo = new employeeMonthSalaryVo();
            employeeMonthSalarVo.setOrderId(order.getId());
            employeeMonthSalarVo.setTime(YearMonth.of(order.getTime().getYear(), order.getTime().getMonth()));
            employeeMonthSalarVo.setOrderName(order.getName());
            Bag bag = bagMap.get(order.getBagId());
            if (bag != null) {
                employeeMonthSalarVo.setBagName(bag.getName());
                employeeMonthSalarVo.setBagImg(bag.getImageUrl());
            }
            //该员工在此订单下的明细与薪水
            List<OrderDetail> details = detailsByOrder.getOrDefault(order.getId(), Collections.emptyList());
            BigDecimal salary = BigDecimal.ZERO;
            List<OrderDetailInfoVo> orderDetailInfoVos = new ArrayList<>();
            for (OrderDetail detail : details) {
                salary = salary.add(detail.getRealPrice().multiply(BigDecimal.valueOf(detail.getRealQuantity())));
                OrderDetailInfoVo orderDetailInfoVo = new OrderDetailInfoVo();
                Process process = processMap.get(detail.getProcessId());
                orderDetailInfoVo.setProcessName(process != null ? process.getName() : "");
                orderDetailInfoVo.setEmployeeName(employeeName);
                orderDetailInfoVo.setRealQuantity(detail.getRealQuantity());
                orderDetailInfoVo.setRealPrice(detail.getRealPrice());
                orderDetailInfoVos.add(orderDetailInfoVo);
            }
            employeeMonthSalarVo.setSalary(salary);
            employeeMonthSalarVo.setOrderDetailList(orderDetailInfoVos);
            employeeMonthSalaryVo.add(employeeMonthSalarVo);
        }
        return pageVo;
    }

    @Override
    @Transactional
    public void deleteEmployee(List<Integer> employeeId) {
        List<OrderDetail> orderDetailList = orderDetailService.lambdaQuery()
                .in(OrderDetail::getEmployeeId, employeeId).list();
        if(!orderDetailList.isEmpty()){
            throw new DeleteExcetion("有订单绑定了该员工，请先删除订单再删除该员工");
        }
        removeByIds(employeeId);
    }

    @Override
    @Transactional
    public void updateEmployee(Employee employee) {
        updateById( employee);
    }
}
