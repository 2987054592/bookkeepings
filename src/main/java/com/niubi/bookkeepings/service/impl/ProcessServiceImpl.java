package com.niubi.bookkeepings.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.niubi.bookkeepings.Excetion.DeleteExcetion;
import com.niubi.bookkeepings.domain.dto.processPageDto;
import com.niubi.bookkeepings.domain.po.OrderDetail;
import com.niubi.bookkeepings.domain.po.Process;
import com.niubi.bookkeepings.domain.po.ProcessBag;
import com.niubi.bookkeepings.domain.vo.processPageVo;
import com.niubi.bookkeepings.mapper.ProcessMapper;
import com.niubi.bookkeepings.service.IOrderDetailService;
import com.niubi.bookkeepings.service.IProcessBagService;
import com.niubi.bookkeepings.service.IProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 工序表 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements IProcessService {
    private final IOrderDetailService orderDetailService;
    private final IProcessBagService processBagService;
    @Override
    public processPageVo pageprocess(processPageDto processPage) {
        processPageVo vo=new processPageVo();
        String name = processPage.getName();
        Integer pageNo = processPage.getPageNo();
        Integer pageSize = processPage.getPageSize();

        Page<Process> page = lambdaQuery()
                .like(name != null, Process::getName, name)
                .page(new Page<>(pageNo, pageSize));
        vo.setProcessList( page.getRecords());
        vo.setTotalData(page.getTotal());
        vo.setTotalPage(page.getPages());
        return vo;
    }

    @Override
    @Transactional
    public void deleteProcess(Integer processId) {
        List<OrderDetail> orderDetailList = orderDetailService.lambdaQuery()
                .eq(OrderDetail::getProcessId, processId).list();
        List<ProcessBag> list = processBagService.lambdaQuery()
                .eq(ProcessBag::getProcessId, processId).list();
        log.info("orderDetailList:{}",orderDetailList);
        if(!orderDetailList.isEmpty()){
            throw new DeleteExcetion("有订单绑定了该工序，请先删除订单再删除该工序");
        }
        if (!list.isEmpty()) {
            throw new DeleteExcetion("有书包绑定了该工序，请先删除书包再删除该工序");
        }
        removeById(processId);
    }

    @Override
    public void updateProcess(Process process) {
        updateById( process);
    }
}
