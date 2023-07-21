package com.leo.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.leo.gulimall.product.vo.AttrRespVo;
import com.leo.gulimall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.leo.gulimall.product.entity.AttrEntity;
import com.leo.gulimall.product.service.AttrService;
import com.leo.common.utils.PageUtils;
import com.leo.common.utils.R;



/**
 * 商品属性
 *
 * @author Leo
 * @email fraudLeo1@Gmail.com
 * @date 2023-07-06 14:58:00
 */
@RestController
@RequestMapping("product/attr")
@Slf4j
public class AttrController {
    @Autowired
    private AttrService attrService;





    @GetMapping("/base/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String,Object> params,@PathVariable("catelogId") Long catelogId) {
       PageUtils page = attrService.queryBaseAttrPage(params,catelogId);
        System.out.println("-------------");

        return R.ok().put("page", page);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo arrtVo){
		attrService.saveAttr(arrtVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
