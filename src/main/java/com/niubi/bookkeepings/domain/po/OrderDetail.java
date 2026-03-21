package com.niubi.bookkeepings.domain.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单详情表
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_detail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工序id
     */
    private String processId;

    /**
     * 主订单id
     */
    private Integer oderId;

    /**
     * 实际价格
     */
    private BigDecimal realPrice;

    /**
     * 实际数量
     */
    private Integer realQuantity;
    /**
     * 员工id
     */
    private Integer employeeId;


}
