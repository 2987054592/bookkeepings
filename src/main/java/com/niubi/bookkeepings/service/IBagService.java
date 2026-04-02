package com.niubi.bookkeepings.service;

import com.niubi.bookkeepings.domain.dto.bagDto;
import com.niubi.bookkeepings.domain.dto.bagPageDto;
import com.niubi.bookkeepings.domain.po.Bag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.niubi.bookkeepings.domain.vo.bagPageVo;
import com.niubi.bookkeepings.domain.vo.bagVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    void saveBage(bagDto bagDto,MultipartFile img) throws Exception;

    bagPageVo pageBag(bagPageDto bagPageDto);

    bagVo getBagById(Integer bagId);


    void deleteBag(Integer bagId) throws Exception;

    void updateBag(bagDto bagDto,MultipartFile img) throws Exception;
}
