package com.demo.dao;

import com.demo.entity.ItemInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ItemInfoMapper {

    /**
     * 获取商品List
     *
     * @return
     */
    public List<ItemInfo> getList();

    /**
     * 添加商品的秒杀库存
     *
     * @return
     */
    public int addLimitStock(ItemInfo itemInfo);

    /**
     * 库存递减
     *
     * @param itemInfo
     * @return
     */
    public int updateStock(ItemInfo itemInfo);

    /**
     * 秒杀库存递减
     *
     * @param itemInfo
     * @return
     */
    public int updateLimitStock(ItemInfo itemInfo);
}
