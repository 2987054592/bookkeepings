package com.niubi.bookkeepings.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderDetailInfoVo {
    private String processName;
    private String employeeName;
    private Integer realQuantity;
    private BigDecimal realPrice;
}
