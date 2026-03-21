package com.niubi.bookkeepings.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailVo {
    private Integer id;
    private Integer oderId;
    private String processName;
    /**
     * 实际价格
     */
    private BigDecimal realPrice;

    /**
     * 实际数量
     */
    private Integer realQuantity;
    private String employeeName;
}
