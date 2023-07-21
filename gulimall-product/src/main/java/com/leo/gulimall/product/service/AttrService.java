package com.leo.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leo.common.utils.PageUtils;
import com.leo.gulimall.product.entity.AttrEntity;
import com.leo.gulimall.product.vo.AttrRespVo;
import com.leo.gulimall.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author Leo
 * @email fraudLeo1@Gmail.com
 * @date 2023-07-06 14:58:00
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo arrtVo);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId);

    AttrRespVo getAttrInfo(Long attrId);
}

