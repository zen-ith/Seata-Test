package com.demo.service.impl;

import com.demo.dao.UserInfoMapper;
import com.demo.entity.UserInfo;
import com.demo.service.UserInfoService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransactionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 账户金额递减
     *
     * @param account
     * @param money
     * @return
     */
    @Override
    public Map<String, Object> decrMoney(String account, int money) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        UserInfo userInfo = new UserInfo();
        userInfo.setAccount(account);
        userInfo.setMoney(money);
        int updateCount = userInfoMapper.update(userInfo);
        log.info("账户金额递减受影响行数：{}", updateCount);

        /**
         try {
         Thread.sleep(30 * 1000);
         } catch (InterruptedException e) {
         e.printStackTrace();
         } // 超时

         int q = 10 / 0; // 异常
         */

        // 金额不足
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
