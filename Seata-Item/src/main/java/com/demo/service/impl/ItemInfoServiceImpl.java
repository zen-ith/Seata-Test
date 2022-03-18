package com.demo.service.impl;

import com.demo.dao.ItemInfoMapper;
import com.demo.entity.ItemInfo;
import com.demo.service.ItemInfoService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransactionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemInfoServiceImpl implements ItemInfoService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemInfoMapper itemInfoMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取商品List
     *
     * @return
     */
    @Override
    public List<ItemInfo> getList() {

        return itemInfoMapper.getList();
    }

    /**
     * 添加商品的秒杀库存，并存到redis中
     *
     * @param id
     * @param count
     * @param price
     * @return
     */
    @Override
    public Map<String, Object> addLimitStock(int id, int count, int price) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        ItemInfo itemInfo = new ItemInfo();
        itemInfo.setId(id);
        itemInfo.setStock(count);
        itemInfo.setPrice(price);

        // 添加商品的秒杀库存
        int updateCount = itemInfoMapper.addLimitStock(itemInfo);
        log.info("添加商品的秒杀库存受影响行数：{}", updateCount);

        // 在库不足
        if (updateCount == 0) {
            // 回滚分布式事务
            if (RootContext.inGlobalTransaction()) {
                try {
                    GlobalTransactionContext.reload(RootContext.getXID()).rollback();
                } catch (TransactionException e) {
                    log.error("回滚分布式事务出现异常：", e);
                }
            }

            resultMap.put("result", false);
            resultMap.put("msg", "在库不足。");
            return resultMap;
        }

        // redis添加商品的秒杀库存
        stringRedisTemplate.opsForValue().increment("ITEM_" + id, count);
        System.out.println("商品id：" + id + "；秒杀数量：" + stringRedisTemplate.opsForValue().get("ITEM_" + id));

        resultMap.put("result", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 库存递减
     *
     * @param id
     * @param count
     * @param isLimit
     * @return
     */
    @Override
    public Map<String, Object> decrCount(int id, int count, boolean isLimit) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        ItemInfo itemInfo = new ItemInfo();
        itemInfo.setId(id);
        itemInfo.setStock(count);

        int updateCount;
        // 库存递减
        if (isLimit) {
            updateCount = itemInfoMapper.updateLimitStock(itemInfo);
        } else {
            updateCount = itemInfoMapper.updateStock(itemInfo);
        }
        log.info("库存递减受影响行数：{}", updateCount);

        // 在库不足
        if (updateCount == 0) {
            // 回滚分布式事务
            if (RootContext.inGlobalTransaction()) {
                try {
                    GlobalTransactionContext.reload(RootContext.getXID()).rollback();
                } catch (TransactionException e) {
                    log.error("回滚分布式事务出现异常：", e);
                }
            }

            resultMap.put("result", false);
            resultMap.put("msg", "在库不足。");
            return resultMap;
        }

        resultMap.put("result", true);
        resultMap.put("msg", "");
        return resultMap;
    }
}
