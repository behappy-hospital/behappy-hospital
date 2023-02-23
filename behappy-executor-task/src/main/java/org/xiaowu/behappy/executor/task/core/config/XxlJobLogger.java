package org.xiaowu.behappy.executor.task.core.config;

import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.core.util.DateUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * TODO: 2022/9/28 修改xxl job日志格式
 * @author xiaowu
 */
@Slf4j
@UtilityClass
public class XxlJobLogger {

    private boolean logDetail(StackTraceElement callInfo, String appendLog) {
        XxlJobContext xxlJobContext = XxlJobContext.getXxlJobContext();
        if (xxlJobContext == null) {
            return false;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(DateUtil.formatDateTime(new Date())).append(" ").append("[" + callInfo.getClassName() + "#" + callInfo.getMethodName() + "]").append("-").append("[" + callInfo.getLineNumber() + "]").append("-").append("[" + Thread.currentThread().getName() + "]").append(" ").append(appendLog != null ? appendLog : "");
            String formatAppendLog = stringBuffer.toString();
            String logFileName = xxlJobContext.getJobLogFileName();
            if (logFileName != null && logFileName.trim().length() > 0) {
                XxlJobFileAppender.appendLog(logFileName, formatAppendLog);
                return true;
            } else {
                log.info(">>>>>>>>>>> {}", formatAppendLog);
                return false;
            }
        }
    }

    public boolean log(String appendLogPattern, Object... appendLogArguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
        String appendLog = ft.getMessage();
        StackTraceElement callInfo = (new Throwable()).getStackTrace()[1];
        return logDetail(callInfo, appendLog);
    }

    public boolean log(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String appendLog = stringWriter.toString();
        StackTraceElement callInfo = (new Throwable()).getStackTrace()[1];
        return logDetail(callInfo, appendLog);
    }
}
