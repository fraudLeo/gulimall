package com.leo.gulimall.product.feign;

import com.leo.common.utils.R;
import com.leo.gulimall.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Component
@FeignClient("gulimall-ware")
public interface WareFeignService {

    @PostMapping("/ware/waresku/hasStock")
    List<SkuHasStockVo> getSkusHasStock(@RequestBody List<Long> skuIds);
}
