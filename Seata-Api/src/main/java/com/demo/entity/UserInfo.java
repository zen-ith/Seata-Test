package com.demo.entity;

import lombok.Data;

/**
 * @Description 用户
 **/
@Data
public class UserInfo {

    private String account;// 账户

    private Integer money;// 余额

    private String name;// 用户名
}
