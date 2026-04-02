package com.niubi.bookkeepings.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.niubi.bookkeepings.Excetion.DeleteExcetion;
import com.niubi.bookkeepings.domain.dto.employeePageDto;
import com.niubi.bookkeepings.domain.po.Employee;
import com.niubi.bookkeepings.domain.po.Order;
import com.niubi.bookkeepings.domain.po.OrderDetail;
import com.niubi.bookkeepings.domain.vo.*;
import com.niubi.bookkeepings.mapper.EmployeeMapper;
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
    private final IOrderDetailService orderDetailService;
    private final IBagService bagService;
    private final ProcessMapper processMapper;
    @Override
    public employeePageVo pageemployee(employeePageDto employeePage) {
        employeePageVo employeePageVo = new employeePageVo();
        String name = employeePage.getName();
        Integer pageSize = employeePage.getPageSize();
        Integer pageNo = employeePage.getPageNo();
        Page<Employee> page = lambdaQuery()
                .like(name != null, Employee::getName, name)
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

    ///根据员工id，根据时间进行分类获得对应的薪水
    public Map<Integer,List<employeeMonthSalary>> getSalaryById(List<Integer> employeeId) {
        Map<Integer,List<employeeMonthSalary>> vo = new HashMap<>();
        for(Integer id:employeeId){
            vo.put(id, new ArrayList<>());
        }
        //获取这个员工的所有订单详情
        List<OrderDetail> orderDetailList = orderDetailService.lambdaQuery()
                .in(OrderDetail::getEmployeeId, employeeId).list();
        if (orderDetailList.isEmpty()) {
            return vo;
        }
        //获取这个员工所有订单的id
        List<Integer> orderId = orderDetailList.stream().map(OrderDetail::getOderId).collect(Collectors.toList());
        //获取这个员工所有订单
        List<Order> orders = orderMapper.selectByIds(orderId);
        for(Integer id:employeeId){
            //组装单个月的薪水vo
            List<employeeMonthSalary> salaryList=new ArrayList<>();
            //key是时间，value是薪水
            Map<YearMonth,BigDecimal> monthlySalaryMap=new HashMap<>();
            for(Order order:orders){
                //初始化
                YearMonth month=YearMonth.of(order.getTime().getYear(), order.getTime().getMonth());
                BigDecimal dailySalary=BigDecimal.ZERO;
                //查询员工对应的订单详情
                List<OrderDetail> details = orderDetailList.stream()
                        .filter(detail -> detail.getEmployeeId().equals(id))
                        .filter(detail -> detail.getOderId().equals(order.getId()))
                        .collect(Collectors.toList());
                //计算这个time的薪水
                dailySalary=details.stream()
                        .map(detail -> detail.getRealPrice().multiply(BigDecimal.valueOf(detail.getRealQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                //如果有薪水，就加入
                if(dailySalary.compareTo(BigDecimal.ZERO)>0){
                    monthlySalaryMap.merge(month,dailySalary, BigDecimal::add);
                }

                //组装结果
            }
            for(Map.Entry<YearMonth,BigDecimal> entry:monthlySalaryMap.entrySet()){
                salaryList.add(new employeeMonthSalary(entry.getKey(),entry.getValue()));
            }
            //插入结果
            vo.put(id,salaryList);
        }
        return vo;
    }

    //查询这个员工薪水的详情
    @Override
    public List<employeeMonthSalaryVo> employeegetById(Integer employeeId) {
        //先确定结果vo
        List<employeeMonthSalaryVo> employeeMonthSalaryVo = new ArrayList<>();
        //查询订单详情
        List<OrderDetail> orderDetailList = orderDetailService.lambdaQuery()
                .eq(OrderDetail::getEmployeeId, employeeId).list();
        //为空就返回空结果
        if (orderDetailList.isEmpty()) {
            return employeeMonthSalaryVo;
        }
        //获取这个员工所有订单的id
        List<Integer> orderId = orderDetailList.stream().map(OrderDetail::getOderId).collect(Collectors.toList());
        //获取这个员工所有订单
        List<Order> orders = orderMapper.selectByIds(orderId);
        for(Order order:orders){
            //组装employeeMonthSalaryVo
            employeeMonthSalaryVo employeeMonthSalarVo = new employeeMonthSalaryVo();
            //设置一些属性
            employeeMonthSalarVo.setTime(YearMonth.of(order.getTime().getYear(), order.getTime().getMonth()));
            employeeMonthSalarVo.setOrderName(order.getName());
            employeeMonthSalarVo.setSalary(SalaryByOrderId(order.getId()));
            employeeMonthSalarVo.setBagName(bagService.getBagById(order.getBagId()).getName());
            employeeMonthSalarVo.setBagImg(bagService.getBagById(order.getBagId()).getImageUrl());
            //组装employeeMonthSalaryVo内的OrderDetailInfoVo集合
            List<OrderDetailInfoVo> orderDetailInfoVos=new ArrayList<>();
            for(OrderDetail detail:orderDetailList){
                if(detail.getOderId().equals(order.getId())){
                    //组装单个vo再添加到list集合当中
                    OrderDetailInfoVo orderDetailInfoVo = new OrderDetailInfoVo();
                    orderDetailInfoVo.setProcessName(processMapper.selectById(detail.getProcessId()).getName());
                    orderDetailInfoVo.setEmployeeName(getById(detail.getEmployeeId()).getName());
                    orderDetailInfoVo.setRealQuantity(detail.getRealQuantity());
                    orderDetailInfoVo.setRealPrice(detail.getRealPrice());
                    orderDetailInfoVos.add(orderDetailInfoVo);
                }
            }
            employeeMonthSalarVo.setOrderDetailList(orderDetailInfoVos);
            employeeMonthSalaryVo.add(employeeMonthSalarVo);
        }


        return employeeMonthSalaryVo;
    }

    //根据订单id，获得这个订单的薪水
    private BigDecimal SalaryByOrderId(Integer orderId) {
        List<OrderDetail> orderDetailList = orderDetailService.lambdaQuery()
                .eq(OrderDetail::getOderId, orderId).list();
        BigDecimal res=BigDecimal.ZERO;
        for (OrderDetail r : orderDetailList) {
            res=res.add(r.getRealPrice().multiply(BigDecimal.valueOf(r.getRealQuantity())));
        }
        return res;
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
