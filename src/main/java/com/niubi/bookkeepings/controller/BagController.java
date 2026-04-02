package com.niubi.bookkeepings.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.niubi.bookkeepings.domain.dto.bagDto;
import com.niubi.bookkeepings.domain.dto.bagPageDto;
import com.niubi.bookkeepings.domain.po.Bag;
import com.niubi.bookkeepings.domain.po.Result;
import com.niubi.bookkeepings.domain.vo.bagPageVo;
import com.niubi.bookkeepings.domain.vo.bagVo;
import com.niubi.bookkeepings.service.IBagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@RestController
@RequestMapping("/bag")
@RequiredArgsConstructor
@Api(tags = "书包相关接口")
public class BagController {
    private final IBagService bagService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @ApiOperation("添加书包")
    @PostMapping
    public Result addBag(@RequestParam("bagDto") String bagDtoJson,@RequestParam(required = false) MultipartFile img) throws Exception {
        bagDto bagDto = objectMapper.readValue(bagDtoJson, bagDto.class);
        bagService.saveBage(bagDto,img);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("分页查询书包")
    public Result<bagPageVo> pageBag(bagPageDto bagPageDto){
        return Result.success(bagService.pageBag(bagPageDto));
    }
    @GetMapping
    @ApiOperation("根据id查询书包")
    private Result<bagVo> getBagById(@RequestParam Integer bagId){
        return Result.success(bagService.getBagById(bagId));
    }
    @GetMapping("/list")
    @ApiOperation("获取书包列表(不带价格和工序)")
    public Result<List<Bag>> listBag(){
        return Result.success(bagService.list());
    }
    @DeleteMapping
    @ApiOperation("删除书包")
    public Result deleteBag(@RequestParam Integer bagId) throws Exception {
        bagService.deleteBag(bagId);
        return Result.success();
    }
    @PostMapping("/update")
    @ApiOperation("更新书包")
    public Result updateBag(@RequestParam("bagDto") String bagDtoJson,@RequestParam(required = false) MultipartFile img) throws Exception {
        bagDto bagDto = objectMapper.readValue(bagDtoJson, bagDto.class);
        bagService.updateBag(bagDto,img);
        return Result.success();
    }

}
