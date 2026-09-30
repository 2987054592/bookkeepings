package com.niubi.bookkeepings.controller;


import cn.hutool.core.bean.BeanUtil;
import com.niubi.bookkeepings.domain.dto.employeePageDto;
import com.niubi.bookkeepings.domain.po.Employee;
import com.niubi.bookkeepings.domain.po.Result;
import com.niubi.bookkeepings.domain.vo.employeeMonthSalaryPageVo;
import com.niubi.bookkeepings.domain.vo.employeePageVo;
import com.niubi.bookkeepings.domain.vo.employeeVo;
import com.niubi.bookkeepings.service.IEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Api(tags = "员工管理")
public class EmployeeController {
    private final IEmployeeService employeeService;
    @PostMapping
    @ApiOperation("添加员工")
    public Result addEmployee(@RequestBody Employee employee){
        employee.setCreateTime(java.time.LocalDateTime.now());
        employeeService.save(employee);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询员工")
    public Result<employeePageVo> page(employeePageDto employeePage){
        return Result.success(employeeService.pageemployee(employeePage));
    }
    @GetMapping("/list")
    @ApiOperation("查询所有员工(不包含薪水，仅给前端展示所有人员)")
    public Result<List<Employee>> list(){
        return Result.success(employeeService.list());
    }
    @GetMapping
    @ApiOperation("根据id分页查询员工工资详情(按订单分页)")
    public Result<employeeMonthSalaryPageVo> getById(@RequestParam Integer employeeId,
                                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize){

        return Result.success(employeeService.employeegetById(employeeId, pageNo, pageSize));
    }
    @DeleteMapping
    @ApiOperation("删除员工")
    public Result deleteEmployee(@RequestParam List<Integer> employeeId){
        employeeService.deleteEmployee(employeeId);
        return Result.success();
    }
    @PostMapping("/update")
    @ApiOperation("修改员工")
    public Result updateEmployee(@RequestBody Employee employee){
        employeeService.updateEmployee(employee);
        return Result.success();
    }

}
