package com.demo.service;

import java.util.Map;

public interface BusinessService {

    /**
     * η§ζδΈε
     *
     * @param account
     * @param id
     * @param count
     * @param price
     * @return
     */
    Map<String, Object> add(String account, int id, int count, int price);
}