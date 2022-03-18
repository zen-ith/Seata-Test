package com.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "Seata-Item", fallback = ItemInfoFeignFallback.class)
public interface ItemInfoFeign {

    /**
     * 库存递减
     *
     * @param id
     * @param count
     * @param isLimit
     * @return
     */
    @PostMapping(value = "/itemInfo/decrCount")
    Map<String, Object> decrCount(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "count") int count,
            @RequestParam(value = "isLimit") boolean isLimit
    );
}
