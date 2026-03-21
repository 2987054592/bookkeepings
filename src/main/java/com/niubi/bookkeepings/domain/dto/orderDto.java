package com.niubi.bookkeepings.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.niubi.bookkeepings.domain.po.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class orderDto {
    private Integer oderId;
    private Integer bagId;
    private String name;
    private LocalDate time;
    private List<OrderDetail> orderDetailList;
}
