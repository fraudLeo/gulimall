package com.example.gulimall.search.controller;

import com.example.gulimall.search.service.ProductSaveService;
import com.leo.common.exception.BizCodeException;
import com.leo.common.to.SkuEsModel;
import com.leo.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/search/save")
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    //上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        System.out.println("到达人生最高峰");
        boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("ElasticSaveController商品上架错误:{}",e.getMessage());
            return  R.error(BizCodeException.PRODUCT_UP_EXCEPTION.getCode(),BizCodeException.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (!b) {
            return R.ok();
        } else {
            return  R.error(BizCodeException.PRODUCT_UP_EXCEPTION.getCode(),BizCodeException.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
