package com.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "Seata-User", fallback = UserInfoFeignFallback.class)
public interface UserInfoFeign {

    /**
     * 账户余额递减
     *
     * @param account
     * @param money
     * @return
     */
    @PostMapping(value = "/userInfo/add")
    Map<String, Object> decrMoney(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "money") int money
    );
}
