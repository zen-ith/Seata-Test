package com.demo.service;

import java.util.List;
import java.util.Map;

import com.demo.entity.ItemInfo;

public interface ItemInfoService {

    /**
     * 获取商品List
     *
     * @return
     */
    List<ItemInfo> getList();

    /**
     * 添加商品的秒杀库存，并存到redis中
     *
     * @param id
     * @param count
     * @param price
     * @return
     */
    Map<String, Object> addLimitStock(int id, int count, int price);

    /**
     * 库存递减
     *
     * @param id
     * @param count
     * @param isLimit
     * @return
     */
    Map<String, Object> decrCount(int id, int count, boolean isLimit);
}
