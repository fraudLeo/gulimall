package com.leo.gulimall.product.service.impl;

import com.leo.common.to.SkuEsModel;
import com.leo.common.to.SkuReductionTo;
import com.leo.common.to.SpuBoundTo;
import com.leo.common.utils.R;
import com.leo.gulimall.product.dao.SpuInfoDescDao;
import com.leo.gulimall.product.entity.*;
import com.leo.gulimall.product.feign.CouponFeignService;
import com.leo.gulimall.product.feign.WareFeignService;
import com.leo.gulimall.product.service.*;
import com.leo.gulimall.product.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leo.common.utils.PageUtils;
import com.leo.common.utils.Query;

import com.leo.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    SpuImagesService imagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService attrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    BrandService brandService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {

        //1. 保存spu基本信息-->pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);
        //2. 保存spu的描述图片-->pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        //3. 保存spu的图片集-->pms_spu_images
        List<String> images = vo.getImages();
        imagesService.saveImages(spuInfoEntity.getId(),images);
        //4. 保存spu的规格-->pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            AttrEntity byId = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(byId.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(spuInfoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);

        //插入:  保存spu的积分信息-->sms_spu_sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode()!=0) {
            log.error("远程保存spu积分信息失败");
        }

        // 5. 保存当前spu对应的所有sku信息
            //5.1 sku的基本信息-->pms_sku_info
        List<Skus> skus = vo.getSkus();

        if (skus!=null &&skus.size()>0) {
            skus.stream().map(sku -> {
                String defaultImg = "";
                for (Images img : sku.getImages()) {
                    if (img.getDefaultImg() == 1) {
                        defaultImg = img.getImgUrl();

                    }
                }

                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);

                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();


                List<SkuImagesEntity> collect1 = sku.getImages().stream().map(item -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(item.getImgUrl());
                    skuImagesEntity.setDefaultImg(item.getDefaultImg());
                    return skuImagesEntity;
                }).filter(item-> {
                    return !StringUtils.isEmpty(item.getImgUrl());
                }).collect(Collectors.toList());
            //5.2 sku的图片信息-->pms_sku_images
                skuImagesService.saveBatch(collect1);
            //5.3 sku的销售属性信息-->pms_sku_sale_attr_value
                List<Attr> attr = sku.getAttr();
                List<SkuSaleAttrValueEntity> collect2 = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());

                skuSaleAttrValueService.saveBatch(collect2);

                //5.4 sku的优惠满减信息-->sms_sku_ladder\sms_sku_full_reduction\sms_member_price\
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount()>0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal(0))==1) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
//                System.out.println(r1+"----------------");
                    if (r1.getCode()!=0) {
                        log.error("远程保存sku优惠信息失败");
                    }

                }

                return spuInfoEntity;
            });
        }


    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity) {

    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
//        System.out.println("----1----");
        log.info(key);
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w-> {
                return w.eq("id",key).or().like("spu_name",key);
            });
        }
//        System.out.println("----2----");
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status",status);
        }
//        System.out.println("----3----");
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)) {
            wrapper.eq("brand_id",brandId);
        }

//        System.out.println("----4----");
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)) {
            //数据库文件拼写都是错的
            wrapper.eq("catalog_id",catelogId);
        }
//        System.out.println("----5----");
        //在下面出现了10000异常,但是控制台没有抛出,逆天
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        System.out.println("--------");
        System.out.println(page.getSize());
        System.out.println("--------");

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {


        // 组装需要的数据
        SkuEsModel skuEsModel = new SkuEsModel();
        //1. 查出当前spuId对应的所有sku信息
        List<SkuInfoEntity> skus = skuInfoService.getSkuBySpuId(spuId);
        List<Long> skuIdList = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        //TODO 4. 查询当前sku所有的规格属性
        List<ProductAttrValueEntity> baseAttrs = attrValueService.baseAttrListforSpu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        List<Long> searchAttrIds = attrService.selectSearchAttrs(attrIds);
        Set<Long> idSet = new HashSet<>(searchAttrIds);

        List<SkuEsModel> skuEsModels = new ArrayList<>();
        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            //返回true就代表满足过滤器条件,不用抛弃
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());

        //TODO 1. 发送远程调用,库存系统查询是否还是有库存
        List<SkuHasStockVo> skusHasStock = wareFeignService.getSkusHasStock(skuIdList);
        hass
        //2. 封装每一个sku的信息(?存疑,这里不就一个嘛
        List<SkuEsModel> upProducts = skus.stream().map(sku-> {
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku,esModel);
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());


            esModel.setHasStock(false);
            //TODO 2. 热点评分 默认新商品0,(这里先统一默认为0,之后逻辑再进行改变)
            esModel.setHotScore(0L);
            //TODO 3. 查询品牌的分类和信息的名字
            BrandEntity brand = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brand.getName());
            esModel.setBrandImg(brand.getLogo());

            CategoryEntity category = categoryService.getById(esModel.getCatalogId());
            esModel.setCatelogName(category.getName());

            //设置检索属性
            esModel.setAttrs(attrsList);
            return esModel;
        }).collect(Collectors.toList());

        //TODO 5. 发给es进行保存,之后查询所有的数据就交给es,即 search微服务进行处理
    }

}