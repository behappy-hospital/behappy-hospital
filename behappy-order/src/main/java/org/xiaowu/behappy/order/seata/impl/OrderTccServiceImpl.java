package org.xiaowu.behappy.order.seata.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.order.seata.OrderTccService;

/**
 * todo
 * @author xiaowu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderTccServiceImpl implements OrderTccService {
    @Override
    public void deduct(String userId, int money) {

    }

    @Override
    public boolean confirm(BusinessActionContext ctx) {
        return false;
    }

    @Override
    public boolean cancel(BusinessActionContext ctx) {
        return false;
    }
}
