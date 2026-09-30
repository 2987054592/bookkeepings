package com.niubi.bookkeepings.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 员工按月工资聚合查询结果 DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalaryAggregateDto {
    private Integer employeeId;
    /**
     * 月份，格式 yyyy-MM
     */
    private String month;
    private BigDecimal salary;
}
