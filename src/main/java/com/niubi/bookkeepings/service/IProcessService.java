package com.niubi.bookkeepings.service;

import com.niubi.bookkeepings.domain.dto.processPageDto;
import com.niubi.bookkeepings.domain.po.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.niubi.bookkeepings.domain.vo.processPageVo;

/**
 * <p>
 * 工序表 服务类
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
public interface IProcessService extends IService<Process> {

    processPageVo pageprocess(processPageDto processPage);

    void deleteProcess(Integer processId);

    void updateProcess(Process process);
}
