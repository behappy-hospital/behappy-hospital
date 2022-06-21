package org.xiaowu.behappy.hosp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.common.vo.SignInfoVo;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.hosp.entity.HospitalSet;
import org.xiaowu.behappy.hosp.mapper.HospitalSetMapper;

/**
 * @author xiaowu
 */
@Service
public class HospitalSetService extends ServiceImpl<HospitalSetMapper, HospitalSet> implements IService<HospitalSet> {

    public String getSignKey(String hoscode) {
        LambdaQueryWrapper<HospitalSet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(HospitalSet::getSignKey);
        queryWrapper.eq(HospitalSet::getHoscode,hoscode);
        return getOne(queryWrapper).getSignKey();
    }

    //获取医院签名信息
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new HospitalException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }

}
