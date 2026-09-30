package com.niubi.bookkeepings.mapper;

import com.niubi.bookkeepings.domain.dto.SalaryAggregateDto;
import com.niubi.bookkeepings.domain.po.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 订单详情表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    /**
     * 子查询分页：取出该员工关联的订单 id（按订单创建时间倒序，id 兜底），只取当前页。
     * 用 GROUP BY 替代 DISTINCT：MySQL 8 要求 DISTINCT 时 ORDER BY 列必须出现在 SELECT 列表中（错误 3065）。
     */
    @Select("SELECT d.oder_id FROM order_detail d JOIN orders o ON d.oder_id = o.id " +
            "WHERE d.employee_id = #{employeeId} " +
            "GROUP BY d.oder_id " +
            "ORDER BY MAX(o.create_time) DESC, d.oder_id DESC LIMIT #{offset}, #{pageSize}")
    List<Integer> selectOrderIdsByEmployeePaged(@Param("employeeId") Integer employeeId,
                                                @Param("offset") Integer offset,
                                                @Param("pageSize") Integer pageSize);

    /**
     * 该员工关联的订单总数（分页总数）
     */
    @Select("SELECT COUNT(DISTINCT oder_id) FROM order_detail WHERE employee_id = #{employeeId}")
    Long countDistinctOrdersByEmployee(@Param("employeeId") Integer employeeId);

    /**
     * 该员工工资总额（全量聚合，一次查出）
     */
    @Select("SELECT COALESCE(SUM(real_price * real_quantity), 0) FROM order_detail WHERE employee_id = #{employeeId}")
    BigDecimal sumSalaryByEmployee(@Param("employeeId") Integer employeeId);

    /**
     * 员工列表页：按 员工 + 月份 聚合工资，替代原来的全量明细查询
     */
    @Select("<script>" +
            "SELECT d.employee_id AS employeeId, DATE_FORMAT(o.time, '%Y-%m') AS month, " +
            "SUM(d.real_price * d.real_quantity) AS salary " +
            "FROM order_detail d JOIN orders o ON d.oder_id = o.id " +
            "WHERE d.employee_id IN " +
            "<foreach collection='employeeIds' item='id' open='(' separator=',' close=')'>#{id}</foreach> " +
            "GROUP BY d.employee_id, month" +
            "</script>")
    List<SalaryAggregateDto> selectSalaryGroupByMonth(@Param("employeeIds") List<Integer> employeeIds);
}
