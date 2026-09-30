package com.niubi.bookkeepings.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 员工工资详情分页 VO
 * 分页维度：订单（按订单去重分页，而不是按明细行）
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class employeeMonthSalaryPageVo {
    /**
     * 当前页订单工资明细
     */
    private List<employeeMonthSalaryVo> orderList;

    /**
     * 该员工关联的订单总数
     */
    private Long totalData;

    /**
     * 总页数（每页 50 单）
     */
    private Long totalPage;

    /**
     * 该员工工资总额（全量，一次聚合查询）
     */
    private BigDecimal totalSalary;

    /**
     * 按月聚合的工资（全量，不随分页变化），key 按 time（yyyy-MM）
     */
    private List<employeeMonthSalary> monthlySalaryList;
}
