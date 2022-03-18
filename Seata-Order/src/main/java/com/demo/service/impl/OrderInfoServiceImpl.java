package com.demo.service.impl;

import com.demo.dao.OrderInfoMapper;
import com.demo.entity.OrderInfo;
import com.demo.feign.ItemInfoFeign;
import com.demo.feign.UserInfoFeign;
import com.demo.service.OrderInfoService;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ItemInfoFeign itemInfoFeign;

    @Autowired
    private UserInfoFeign userInfoFeign;

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
    @GlobalTransactional(name = "seata_test_tx_group", rollbackFor = Exception.class)
    @Override
    public Map<String, Object> add(String account, int id, int count, int price, boolean isLimit) {

        Map<String, Object> resultMap;

        // 添加订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAccount(account);
        orderInfo.setMessage("生成订单");
        orderInfo.setMoney(price * count);
        orderInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        int insertCount = orderInfoMapper.add(orderInfo);
        log.info("添加订单受影响行数：{}", insertCount);

        // 递减库存
        resultMap = itemInfoFeign.decrCount(id, count, isLimit);
        if (!(Boolean) resultMap.get("result")){
            return resultMap;
        }

        // 用户账户余额递减
        resultMap = userInfoFeign.decrMoney(account, price * count);
        if (!(Boolean) resultMap.get("result")){
            return resultMap;
        }

        resultMap.put("result", true);
        resultMap.put("msg", "");
        return resultMap;
    }
}
