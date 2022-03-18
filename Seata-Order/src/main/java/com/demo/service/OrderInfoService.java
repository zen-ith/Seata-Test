package com.demo.service;

import java.util.Map;

public interface OrderInfoService {

    /**
     * 添加订单
     *
     * @param account
     * @param id
     * @param count
     * @param price
     * @param isLimit
     * @return
     */
    Map<String, Object> add(String account, int id, int count, int price, boolean isLimit);
}
