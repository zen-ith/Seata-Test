package com.demo.controller;

import com.demo.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orderInfo")
@CrossOrigin
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

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
    @PostMapping(value = "/add")
    public Map<String, Object> add(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "id") int id,
            @RequestParam(value = "count") int count,
            @RequestParam(value = "price") int price,
            @RequestParam(value = "isLimit") boolean isLimit) {

        return orderInfoService.add(account, id, count, price, isLimit);
    }

}
