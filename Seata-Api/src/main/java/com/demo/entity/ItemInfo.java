package com.demo.entity;

import lombok.Data;

/**
 * @Description 商品信息
 */
@Data
public class ItemInfo {

    private Integer id;// 商品id

    private String name;// 商品名称

    private Integer stock;// 商品数量（普通在库）

    private Integer stock_limit;// 商品数量（秒杀在库）

    private Integer price;// 商品价格
}