package com.niubi.bookkeepings.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderVos {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 订单名称
     */
    private String name;

    /**
     * 订单时间
     */
    private YearMonth times;

    /**
     * 书包名称
     */
    private Integer bagId;
    private String bagName;
    private String imageUrl;
}
