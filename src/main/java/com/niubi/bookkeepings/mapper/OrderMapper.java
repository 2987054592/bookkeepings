package com.niubi.bookkeepings.mapper;

import com.niubi.bookkeepings.domain.po.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("select * from order where bag_id = #{bagId}")
    List<Order> selectByBagId(Integer bagId);
}
