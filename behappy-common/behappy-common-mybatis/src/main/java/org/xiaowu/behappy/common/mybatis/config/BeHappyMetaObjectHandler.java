package org.xiaowu.behappy.common.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 小五
 */
public class BeHappyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        boolean needConvertCreateTime = needConvertCreateTime(metaObject);
        if (needConvertCreateTime) {
            Class<?> createTimeType = getCreateTimeType(metaObject);
            if (createTimeType.equals(LocalDateTime.class)){
                strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            }else {
                strictInsertFill(metaObject, "createTime", Date.class, new Date());
            }
        }
        this.updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        boolean needConvertUpdateTime = needConvertUpdateTime(metaObject);
        if (needConvertUpdateTime) {
            Class<?> updateTimeType = getUpdateTimeType(metaObject);
            if (updateTimeType.equals(LocalDateTime.class)){
                strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }else {
                strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
            }
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

    private Class<?> getCreateTimeType(MetaObject metaObject) {
        return metaObject.getSetterType("createTime");
    }

    private Class<?> getUpdateTimeType(MetaObject metaObject) {
        return metaObject.getSetterType("updateTime");
    }
}
