package org.xiaowu.behappy.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.user.entity.UserLoginRecord;
import org.xiaowu.behappy.user.mapper.UserLoginRecordMapper;

/**
 * @author xiaowu
 */
@Service
public class UserLoginRecordService extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements IService<UserLoginRecord> {
}
