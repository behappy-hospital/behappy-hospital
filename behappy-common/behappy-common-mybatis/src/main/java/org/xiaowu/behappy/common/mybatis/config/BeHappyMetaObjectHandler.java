package org.xiaowu.behappy.common.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author 小五
 */
public class BeHappyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        boolean needConvertCreateTime = needConvertCreateTime(metaObject);
        if (needConvertCreateTime) {
            strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
        boolean needConvertUpdateTime = needConvertUpdateTime(metaObject);
        if (needConvertUpdateTime) {
            strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        boolean needConvertUpdateTime = needConvertUpdateTime(metaObject);
        if (needConvertUpdateTime) {
            strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }

    private boolean needConvertUpdateTime(MetaObject metaObject) {
        boolean updateTimeHasSetter = metaObject.hasSetter("updateTime");
        Object updateTimeHasVal = getFieldValByName("updateTime", metaObject);
        boolean flag = updateTimeHasSetter && updateTimeHasVal == null;
        return flag;
    }

    private boolean needConvertCreateTime(MetaObject metaObject) {
        // 看实体类中是否有这个属性，有的话就执行,没有就不执行
        boolean createTimeHasSetter = metaObject.hasSetter("createTime");
        // 如果预先自己设置了值,则设置不使用MP的自动填充
        Object createTimeHasVal = getFieldValByName("createTime", metaObject);
        boolean flag = createTimeHasSetter && createTimeHasVal == null;
        return flag;
    }
}