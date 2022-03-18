package com.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "Seata-Order", fallback = OrderInfoFeignFallback.class)
public interface OrderInfoFeign {

    /**
     * 增加订单
     *
     * @param account
     * @param id
     * @param count
     * @param price
     * @param isLimit
     * @return
     */
    @PostMapping(value = "/orderInfo/add")
    Map<String, Object> add(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "id") int id,
            @RequestParam(value = "count") int count,
            @RequestParam(value = "price") int price,
            @RequestParam(value = "isLimit") boolean isLimit
    );
}
