package com.niubi.bookkeepings.domain.vo;

import com.niubi.bookkeepings.domain.po.Order;
import com.niubi.bookkeepings.domain.query.PageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class orderPageVo extends PageVo {
    private List<OrderVos> orderList;
}
