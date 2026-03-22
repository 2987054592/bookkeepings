package com.niubi.bookkeepings.domain.vo;

import com.niubi.bookkeepings.domain.po.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class employeeMonthSalaryVo {
    private YearMonth time;
    private String orderName;
    private String bagName;
    private BigDecimal salary;
    List<OrderDetailInfoVo> orderDetailList;
}
