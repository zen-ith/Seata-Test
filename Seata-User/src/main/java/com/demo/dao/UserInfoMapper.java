package com.demo.dao;

import com.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserInfoMapper {

    /**
     * 账户金额递减
     *
     * @param userInfo
     * @return
     */
    public int update(UserInfo userInfo);
}
