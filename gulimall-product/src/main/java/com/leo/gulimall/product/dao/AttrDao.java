package com.leo.gulimall.product.dao;

import com.leo.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author Leo
 * @email fraudLeo1@Gmail.com
 * @date 2023-07-06 14:58:00
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectSearchAttreIds(@Param("attrIds") List<Long> attrIds);
}
