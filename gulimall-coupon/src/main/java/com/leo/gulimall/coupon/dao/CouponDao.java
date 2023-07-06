package com.leo.gulimall.coupon.dao;

import com.leo.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author Leo
 * @email fraudLeo1@Gmail.com
 * @date 2023-07-06 17:23:52
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
