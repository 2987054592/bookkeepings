package com.niubi.bookkeepings.controller;


import com.niubi.bookkeepings.domain.dto.orderDto;
import com.niubi.bookkeepings.domain.dto.orderPageDto;
import com.niubi.bookkeepings.domain.po.Order;
import com.niubi.bookkeepings.domain.po.Result;
import com.niubi.bookkeepings.domain.vo.orderPageVo;
import com.niubi.bookkeepings.domain.vo.orderVo;
import com.niubi.bookkeepings.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Api(tags = "订单管理")
public class OrderController {
    private final IOrderService orderService;
    @PostMapping
    @ApiOperation("添加订单")
    public Result addOrder(@RequestBody orderDto order){
        orderService.addOrder(order);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询订单")
    public Result<orderPageVo> pageOrder(orderPageDto orderPage){
        return Result.success(orderService.pageOrder(orderPage));
    }
    @GetMapping
    @ApiOperation("根据id查询订单")
    public Result<orderVo> getOrderById(@RequestParam Integer orderId){
        return Result.success(orderService.getOrderById(orderId));
    }
    @DeleteMapping
    @ApiOperation("删除订单")
    public Result deleteOrder(@RequestParam List<Integer> orderId){
        orderService.deleteOrder(orderId);
        return Result.success();
    }
    @PostMapping("/update")
    @ApiOperation("修改订单")
    public Result updateOrder(@RequestBody orderDto order){
        orderService.updateOrder(order);
        return Result.success();
    }

}
