package com.leo.gulimall.product.dao;

import com.leo.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author Leo
 * @email fraudLeo1@Gmail.com
 * @date 2023-07-06 14:58:00
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
