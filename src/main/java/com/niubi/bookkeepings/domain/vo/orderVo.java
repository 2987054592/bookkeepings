package com.niubi.bookkeepings.domain.vo;

import com.niubi.bookkeepings.domain.po.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class orderVo {
    private Integer oderId;
    private String bagName;
    private String imageUrl;
    private String name;
    private YearMonth time;
    private List<OrderDetailVo> orderDetailVoList;
}
