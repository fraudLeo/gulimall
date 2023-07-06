package com.leo.gulimall.order.dao;

import com.leo.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author Leo
 * @email fraudLeo1@Gmail.com
 * @date 2023-07-06 17:54:36
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
