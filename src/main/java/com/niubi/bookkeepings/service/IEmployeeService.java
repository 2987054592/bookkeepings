package com.niubi.bookkeepings.service;

import com.niubi.bookkeepings.domain.dto.employeePageDto;
import com.niubi.bookkeepings.domain.po.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.niubi.bookkeepings.domain.vo.employeeMonthSalary;
import com.niubi.bookkeepings.domain.vo.employeeMonthSalaryVo;
import com.niubi.bookkeepings.domain.vo.employeePageVo;
import com.niubi.bookkeepings.domain.vo.employeeVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
public interface IEmployeeService extends IService<Employee> {

    employeePageVo pageemployee(employeePageDto employeePage);

    

    Map<Integer,List<employeeMonthSalary>> getSalaryById(List<Integer> employeeId);

    List<employeeMonthSalaryVo> employeegetById(Integer employeeId);

    void deleteEmployee(List<Integer> employeeId);

    void updateEmployee(Employee employee);
}
