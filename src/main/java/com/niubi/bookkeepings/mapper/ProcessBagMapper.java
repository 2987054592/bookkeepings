package com.niubi.bookkeepings.mapper;

import com.niubi.bookkeepings.domain.po.ProcessBag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 书包和工序关联表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
@Mapper
public interface ProcessBagMapper extends BaseMapper<ProcessBag> {

}
