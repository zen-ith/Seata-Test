package com.demo.service;

import java.util.Map;

public interface BusinessService {

    /**
     * 秒杀下单
     *
     * @param account
     * @param id
     * @param count
     * @param price
     * @return
     */
    Map<String, Object> add(String account, int id, int count, int price);
}