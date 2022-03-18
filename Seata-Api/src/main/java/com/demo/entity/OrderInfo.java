package com.demo.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description 订单
 */
@Data
public class OrderInfo {

    private Integer id;// 订单id

    private String account;// 账户

    private String message;// 留言

    private Integer money;// 总金额

    private Timestamp createTime;// 创建时间
}

