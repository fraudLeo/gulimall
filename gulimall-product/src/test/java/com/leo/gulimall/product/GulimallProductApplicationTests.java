package com.leo.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leo.gulimall.product.entity.BrandEntity;
import com.leo.gulimall.product.service.BrandService;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Configuration
@SpringBootTest
@ComponentScan(basePackages = {"com.leo.gulimall.product.service"})
public class GulimallProductApplicationTests {


    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("魅族");
//        brandService.save(brandEntity);
//        log.info("保存成功，{}",brandEntity.toString());

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id",9L));
        list.forEach((item)->{log.info("品牌id查询，{}",item);
            System.out.println(item);});
    }

}
