package com.demo.dao;

import com.demo.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderInfoMapper {

    /**
     * 添加订单
     *
     * @param orderInfo
     * @return
     */
    public int add(OrderInfo orderInfo);
}
