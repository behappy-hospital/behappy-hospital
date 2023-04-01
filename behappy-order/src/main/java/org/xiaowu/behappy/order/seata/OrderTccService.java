package org.xiaowu.behappy.order.seata;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author xiaowu
 */
@LocalTCC
public interface OrderTccService {

    @TwoPhaseBusinessAction(name = "deduct", commitMethod = "confirm", rollbackMethod = "cancel", useTCCFence = true)
    void deduct(@BusinessActionContextParameter(paramName = "userId") String userId,
                @BusinessActionContextParameter(paramName = "money")int money);

    /**
     * 确认方法、可以另命名，但要保证与commitMethod一致
     * context可以传递try方法的参数
     *
     * @param ctx 上下文
     * @return boolean
     */
    boolean confirm(BusinessActionContext ctx);

    /**
     * 二阶段取消方法
     *
     * @param ctx 上下文
     * @return boolean
     */
    boolean cancel(BusinessActionContext ctx);
}
