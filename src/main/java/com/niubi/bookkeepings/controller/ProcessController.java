package com.niubi.bookkeepings.controller;


import com.niubi.bookkeepings.domain.dto.processPageDto;
import com.niubi.bookkeepings.domain.po.Process;
import com.niubi.bookkeepings.domain.po.Result;
import com.niubi.bookkeepings.domain.vo.processPageVo;
import com.niubi.bookkeepings.service.IProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 工序表 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@RestController
@RequestMapping("/process")
@RequiredArgsConstructor
@Api(tags = "工序管理")
public class ProcessController {
    private final IProcessService processService;
    @PostMapping
    @ApiOperation("添加工序")
    public Result addProcess(@RequestBody Process process){
        processService.save(process);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询工序")
    public Result<processPageVo> pageprocess(processPageDto processPage){
        return Result.success(processService.pageprocess(processPage));
    }
    @GetMapping("/list")
    @ApiOperation("查询所有工序")
    public Result<List<Process>> listprocess(){
        return Result.success(processService.list());
    }
    @GetMapping
    @ApiOperation("根据id查询工序")
    public Result<Process> getProcessById(@RequestParam Integer processId){
        return Result.success(processService.getById(processId));
    }
    @DeleteMapping
    @ApiOperation("删除工序")
    public Result deleteProcess(@RequestParam List<Integer> processId){
        processService.deleteProcess(processId);
        return Result.success();
    }
    @PostMapping("/update")
    @ApiOperation("更新工序")
    public Result updateProcess(@RequestBody Process process){
        processService.updateProcess(process);
        return Result.success();
    }


}
