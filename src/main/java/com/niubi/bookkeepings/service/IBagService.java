package com.niubi.bookkeepings.service;

import com.niubi.bookkeepings.domain.dto.bagDto;
import com.niubi.bookkeepings.domain.dto.bagPageDto;
import com.niubi.bookkeepings.domain.po.Bag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.niubi.bookkeepings.domain.vo.bagPageVo;
import com.niubi.bookkeepings.domain.vo.bagVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2026-03-21
 */
public interface IBagService extends IService<Bag> {

    void saveBage(bagDto bagDto);

    bagPageVo pageBag(bagPageDto bagPageDto);

    bagVo getBagById(Integer bagId);


    void deleteBag(Integer bagId);

    void updateBag(bagDto bagDto);
}
