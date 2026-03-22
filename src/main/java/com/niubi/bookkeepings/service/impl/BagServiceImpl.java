package com.niubi.bookkeepings.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.niubi.bookkeepings.Excetion.DeleteExcetion;
import com.niubi.bookkeepings.domain.dto.bagDto;
import com.niubi.bookkeepings.domain.dto.bagPageDto;
import com.niubi.bookkeepings.domain.dto.processDto;
import com.niubi.bookkeepings.domain.po.*;
import com.niubi.bookkeepings.domain.po.Process;
import com.niubi.bookkeepings.domain.vo.bagPageVo;
import com.niubi.bookkeepings.domain.vo.bagVo;
import com.niubi.bookkeepings.mapper.BagMapper;
import com.niubi.bookkeepings.mapper.OrderDetailMapper;
import com.niubi.bookkeepings.mapper.OrderMapper;
import com.niubi.bookkeepings.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BagServiceImpl extends ServiceImpl<BagMapper, Bag> implements IBagService {
    private final IProcessBagService processBagService;
    private final IProcessService processService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public void saveBage(bagDto bagDto) {
        Bag bag = BeanUtil.copyProperties(bagDto, Bag.class);
        save(bag);
        List<processDto> processList1 = bagDto.getProcessList();
        List<ProcessBag> processBagList = new ArrayList<>();
        for (processDto p : processList1) {
            ProcessBag processBag = new ProcessBag();
            processBag.setBagId(bag.getId());
            processBag.setProcessId(p.getId());
            processBag.setDefaultPrice(p.getPrice());
            processBagList.add(processBag);
        }
        processBagService.saveBatch(processBagList);

    }

    @Override
    public bagPageVo pageBag(bagPageDto bagPage) {
        log.info("分页查询书包信息");
        String name = bagPage.getName();
        Page<Bag> page = lambdaQuery().like(name != null, Bag::getName, name)
                .page(new Page<>(
                        bagPage.getPageNo(),
                        bagPage.getPageSize()
                ));
        if (page.getRecords() == null || page.getRecords().isEmpty()) {
            bagPageVo bagPageVo = new bagPageVo();
            bagPageVo.setTotalPage(0);
            bagPageVo.setTotalData(0);
            bagPageVo.setBagList(Collections.emptyList());
            return bagPageVo;
        }

        List<Bag> records = page.getRecords();
        //获取所有书包的id
        List<Integer> bagIds = records.stream().map(Bag::getId).collect(Collectors.toList());
        //获取所有包含书包id的工序
        List<ProcessBag> processBags = processBagService.lambdaQuery().in(ProcessBag::getBagId, bagIds).list();
        //根据书包id分组
        Map<Integer, List<ProcessBag>> processMap = processBags.stream().collect(Collectors.groupingBy(ProcessBag::getBagId));
        //组装vo返回前端
        bagPageVo vo = new bagPageVo();
        //组装vo需要的工序集合
        List<bagVo> bagVos = new ArrayList<>();
        for (Bag r : records) {
            //组装单个vo
            bagVo bagVo = new bagVo();
            bagVo.setId(r.getId());
            bagVo.setName(r.getName());
            //获取这个书包的所有工序书包关联表
            List<ProcessBag> processBags1 = processMap.get(r.getId());
            //key为工序id，value为工序和书包的关联（用于获取默认价格）
            Map<Integer, ProcessBag> collectmap = processBags1.stream().collect(Collectors.toMap(ProcessBag::getProcessId, p -> p));
            //获取所有工序的id
            List<Integer> processIds = processBags1.stream().map(ProcessBag::getProcessId).collect(Collectors.toList());
            log.info("processIds:{}", processIds);
            //查询该工序
            List<Process> processes = processService.listByIds(processIds);

            List<processDto> processDtos = new ArrayList<>();
            for (Process p : processes) {
                processDto processDto = BeanUtil.copyProperties(p, processDto.class);
                processDto.setPrice(collectmap.get(p.getId()).getDefaultPrice());
                processDtos.add(processDto);
            }

            bagVo.setProcessList(processDtos);
            bagVos.add(bagVo);

        }
        vo.setBagList(bagVos);
        vo.setTotalPage(page.getPages());
        vo.setTotalData(page.getTotal());
        return vo;
    }

    @Override
    public bagVo getBagById(Integer bagId) {
        bagVo vo = new bagVo();
        Bag bag = getById(bagId);
        vo.setId(bag.getId());
        vo.setName(bag.getName());
        List<processDto> processList = new ArrayList<>();
        List<ProcessBag> list = processBagService.lambdaQuery()
                .eq(ProcessBag::getBagId, bagId).list();
        for (ProcessBag p : list) {
            Process process = processService.getById(p.getProcessId());
            processDto processDto = BeanUtil.copyProperties(process, processDto.class);
            processDto.setPrice(p.getDefaultPrice());
            processList.add(processDto);
        }
        vo.setProcessList(processList);
        return vo;
    }

    @Override
    public void deleteBag(List<Integer> bagId) {
        List<Order> order = orderMapper.selectByIds(bagId);
        if (order != null && !order.isEmpty()) {
            throw new DeleteExcetion("请先删除该书包下的订单");
        }
        removeByIds(bagId);
    }

    @Transactional
    @Override
    public void updateBag(bagDto bagDto) {
        Bag bag = BeanUtil.copyProperties(bagDto, Bag.class);
        updateById(bag);
        processBagService.lambdaUpdate()
                .eq(ProcessBag::getBagId, bag.getId())
                .remove();
        List<processDto> processList1 = bagDto.getProcessList();
        List<ProcessBag> processBagList = new ArrayList<>();
        for (processDto p : processList1) {
            ProcessBag processBag = new ProcessBag();
            processBag.setBagId(bag.getId());
            processBag.setProcessId(p.getId());
            processBag.setDefaultPrice(p.getPrice());
            processBagList.add(processBag);
        }
        processBagService.saveBatch(processBagList);
    }
}
