package org.xiaowu.behappy.xxl.admin.core.alarm;

import org.xiaowu.behappy.xxl.admin.core.model.XxlJobInfo;
import org.xiaowu.behappy.xxl.admin.core.model.XxlJobLog;

/**
 * @author xuxueli 2020-01-19
 */
public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog);

}
