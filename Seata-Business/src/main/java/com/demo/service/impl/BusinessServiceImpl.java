package com.demo.service.impl;

import com.demo.feign.OrderInfoFeign;
import com.demo.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private OrderInfoFeign orderInfoFeign;

    /**
     * 秒杀下单
     *
     * @param account
     * @param id
     * @param count
     * @param price
     * @return
     */
    @Override
    public Map<String, Object> add(String account, int id, int count, int price) {
        // 添加订单
        return orderInfoFeign.add(account, id, count, price, true);
    }
}
