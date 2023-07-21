package com.leo.gulimall.product.service.impl;

import com.leo.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leo.common.utils.PageUtils;
import com.leo.common.utils.Query;

import com.leo.gulimall.product.dao.BrandDao;
import com.leo.gulimall.product.entity.BrandEntity;
import com.leo.gulimall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> objectQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            objectQueryWrapper.eq("brand_id",key).or().like("name",key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                objectQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(BrandEntity brand) {
        //保证冗余字段的数据一致
        this.updateById(brand);
        //同步更新其它关联表中的数据
        categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());

        //TODO更新其他关联
    }

}