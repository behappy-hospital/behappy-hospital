package org.xiaowu.behappy.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.hosp.model.HospitalSet;
import org.xiaowu.behappy.hosp.mapper.HospitalSetMapper;

/**
 * @author xiaowu
 */
@Service
public class HospitalSetService extends ServiceImpl<HospitalSetMapper, HospitalSet> implements IService<HospitalSet> {

}
