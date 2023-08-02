package com.leo.gulimall.product.feign;

import com.leo.common.to.SkuEsModel;
import com.leo.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/prodict")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
