package com.demo.controller;

import com.demo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/userInfo")
@CrossOrigin
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 账户余额递减
     *
     * @param account
     * @param money
     * @return
     */
    @PostMapping(value = "/add")
    public Map<String, Object> decrMoney(@RequestParam(value = "account") String account, @RequestParam(value = "money") int money) {

        return userInfoService.decrMoney(account, money);
    }

}
