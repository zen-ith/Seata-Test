package com.demo.service;

import java.util.Map;

public interface UserInfoService {

    /**
     * 账户金额递减
     *
     * @param account
     * @param money
     * @return
     */
    Map<String, Object> decrMoney(String account, int money);
}
