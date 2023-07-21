package com.leo.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leo.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.leo.gulimall.product.dao.AttrGroupDao;
import com.leo.gulimall.product.dao.CategoryDao;
import com.leo.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.leo.gulimall.product.entity.AttrGroupEntity;
import com.leo.gulimall.product.entity.CategoryEntity;
import com.leo.gulimall.product.service.CategoryService;
import com.leo.gulimall.product.vo.AttrRespVo;
import com.leo.gulimall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

import com.leo.gulimall.product.dao.AttrDao;
import com.leo.gulimall.product.entity.AttrEntity;
import com.leo.gulimall.product.service.AttrService;


@Service("attrService")
@Slf4j
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {


    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.save(attrEntity);
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrId(attrVo.getAttrGroupId());
        attrAttrgroupRelationEntity.setId(attrAttrgroupRelationEntity.getAttrId());

        attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<>();

        if (catelogId != 0) {
            attrEntityQueryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");

        if (!StringUtils.isEmpty(key)) {
            attrEntityQueryWrapper.and((wapper) -> {
                return wapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                attrEntityQueryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            //分组
            AttrAttrgroupRelationEntity attr_id =
                    attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                            .eq("attr_id", attrEntity.getAttrId()));
            if (attr_id != null) {
                attrRespVo.setGroupName(attrGroupDao.selectById(attr_id.getAttrGroupId()).getAttrGroupName());
            }
            //分类
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(respVos);
        log.info(respVos.toString());
    return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo respVo = new AttrRespVo();
        AttrEntity byId = this.getById(attrId);

        BeanUtils.copyProperties(byId,respVo);
        //分组信息
        AttrAttrgroupRelationEntity attr_id = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if (attr_id!=null) {
            respVo.setAttrGroupId(attr_id.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attr_id.getAttrGroupId());
            if (attrGroupEntity!=null) {
                respVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
        //分类信息
        Long catelogId = byId.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        respVo.setPath(catelogPath);

        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity!=null) {
            respVo.setAttrName(categoryEntity.getName());
        }

        return respVo;
    }

}