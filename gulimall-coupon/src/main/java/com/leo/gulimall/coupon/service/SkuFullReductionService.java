package com.leo.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leo.common.to.SkuReductionTo;
import com.leo.common.utils.PageUtils;
import com.leo.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author Leo
 * @email fraudLeo1@Gmail.com
 * @date 2023-07-06 17:23:52
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

