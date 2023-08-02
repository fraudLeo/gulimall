package com.leo.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leo.common.utils.PageUtils;
import com.leo.gulimall.ware.entity.PurchaseDetailEntity;
import com.leo.gulimall.ware.entity.WareSkuEntity;
import com.leo.gulimall.ware.vo.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author Leo
 * @email fraudLeo1@Gmail.com
 * @date 2023-07-06 18:00:02
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}

