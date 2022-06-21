package org.xiaowu.behappy.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaowu.behappy.manager.entity.HospitalSet;

import java.util.Map;

public interface HospitalService extends IService<HospitalSet> {

    HospitalSet getFirst();

    /**
     * 预约下单
     * @param paramMap
     * @return
     */
    Map<String, Object> submitOrder(Map<String, Object> paramMap);

    /**
     * 更新支付状态
     * @param paramMap
     */
    void updatePayStatus(Map<String, Object> paramMap);

    /**
     * 更新取消预约状态
     * @param paramMap
     */
    void updateCancelStatus(Map<String, Object> paramMap);


}
