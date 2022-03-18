package com.demo.feign;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransactionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserInfoFeignFallback implements UserInfoFeign {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${server.port}")
    private String port;

    @Override
    public Map<String, Object> decrMoney(String account, int money) {

        log.error("用户服务[{}]失败，用户：{}", port, account);

        // 回滚分布式事务
        if (RootContext.inGlobalTransaction()) {
            try {
                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            } catch (TransactionException e) {
                log.error("回滚分布式事务出现异常：", e);
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", false);
        resultMap.put("msg", "用户服务调用失败。");
        return resultMap;
    }
}