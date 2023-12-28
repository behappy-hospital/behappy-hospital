package org.xiaowu.behappy.hosp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.common.vo.SignInfoVo;
import org.xiaowu.behappy.api.hosp.constants.HospConstant;
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
        HospitalSet hospitalSet = getHospitalSet(hoscode);
        if(null == hospitalSet) {
            throw new HospitalException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        return hospitalSet.getSignKey();
    }

    //获取医院签名信息
    public SignInfoVo getSignInfoVo(String hoscode) {
        HospitalSet hospitalSet = getHospitalSet(hoscode);
        if(null == hospitalSet) {
            throw new HospitalException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }

    // @Cacheable(cacheNames = HospConstant.HOSP_CACHE, key = "#hoscode")
    public HospitalSet getHospitalSet(String hoscode) {
        LambdaQueryWrapper<HospitalSet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HospitalSet::getHoscode,hoscode);
        return getOne(queryWrapper);
    }


    /*@CachePut(cacheNames = HospConstant.HOSP_CACHE, key = "#hospitalSet.hoscode")
    public HospitalSet saveHospitalSet(HospitalSet hospitalSet) {
        return hospitalSet;
    }

    @CacheEvict(cacheNames = HospConstant.HOSP_CACHE, key = "#hoscode")
    public void delHospitalSet(String hoscode) {
    }*/
}
