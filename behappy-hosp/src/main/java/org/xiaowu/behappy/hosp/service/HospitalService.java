package org.xiaowu.behappy.hosp.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.hosp.model.Hospital;
import org.xiaowu.behappy.hosp.repository.HospitalRepository;

import java.util.Date;
import java.util.Map;

/**
 * @author xiaowu
 */
@Service
@Slf4j
@AllArgsConstructor
public class HospitalService {

    private final ObjectMapper objectMapper;

    private final HospitalRepository hospitalRepository;

    @SneakyThrows
    public void saveHosp(Map<String, Object> map) {
        Hospital hospital = objectMapper.readValue(objectMapper.writeValueAsString(map), Hospital.class);
        // 判断是否存在
        Hospital targetHospital = hospitalRepository.findHospitalByHoscode(hospital.getHoscode());
        // 存在, 更新
        if (null != targetHospital){
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        }else {
            // 不存在, 新增
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        }
        hospitalRepository.save(hospital);
    }

    public Hospital findHospitalByHoscode(String hoscode){
        return hospitalRepository.findHospitalByHoscode(hoscode);
    }


}
