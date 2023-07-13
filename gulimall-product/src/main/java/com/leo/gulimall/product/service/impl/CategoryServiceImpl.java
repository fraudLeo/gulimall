package com.leo.gulimall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leo.common.utils.PageUtils;
import com.leo.common.utils.Query;

import com.leo.gulimall.product.dao.CategoryDao;
import com.leo.gulimall.product.entity.CategoryEntity;
import com.leo.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        List<CategoryEntity> categoryEntities = categoryDao.selectList(null);

        //需要组装成父子的树形结构
            //找到所有的一级分类
        List<CategoryEntity> list = categoryEntities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == 0;
        }).map((menu)-> {
            menu.setChildren(getChildrens(menu,categoryEntities));
            return menu;
        }).sorted((menu1,menu2)-> {
            return (menu1.getSort() == null?0: menu1.getSort()) - (menu2.getSort() == null?0: menu2.getSort());
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前待删除字段是否被引用

        categoryDao.deleteBatchIds(asList);
    }

    //查询所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //找子菜单的递归
            categoryEntity.setChildren(getChildrens(categoryEntity,all));
            return categoryEntity;
        }).sorted((menu1,menu2) -> {
            return (menu1.getSort() == null?0: menu1.getSort()) - (menu2.getSort() == null?0: menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }
}