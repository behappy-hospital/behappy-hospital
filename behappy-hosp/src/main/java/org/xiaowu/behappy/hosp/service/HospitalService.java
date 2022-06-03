package org.xiaowu.behappy.hosp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.hosp.model.Hospital;
import org.xiaowu.behappy.api.hosp.vo.HospitalQueryVo;
import org.xiaowu.behappy.common.core.enums.DictEnum;
import org.xiaowu.behappy.common.core.result.Response;
import org.xiaowu.behappy.common.core.util.ResponseConvert;
import org.xiaowu.behappy.hosp.feign.DictFeign;
import org.xiaowu.behappy.hosp.repository.HospitalRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiaowu
 */
@Service
@Slf4j
@AllArgsConstructor
public class HospitalService {

    private final ObjectMapper objectMapper;

    private final HospitalRepository hospitalRepository;

    private final DictFeign dictFeign;

    private final ThreadPoolExecutor executor;

    private final ResponseConvert responseConvert;

    @SneakyThrows
    public void saveHosp(Map<String, Object> map) {
        Hospital hospital = objectMapper.readValue(objectMapper.writeValueAsString(map), Hospital.class);
        // 判断是否存在
        Hospital targetHospital = hospitalRepository.findHospitalByHoscode(hospital.getHoscode());
        // 存在, 更新
        if (null != targetHospital) {
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        } else {
            // 不存在, 新增
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        }
        hospitalRepository.save(hospital);
    }

    public Hospital findHospitalByHoscode(String hoscode) {
        return hospitalRepository.findHospitalByHoscode(hoscode);
    }

    public Page<Hospital> listPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = PageRequest.of(page - 1, limit, sort);
        Hospital hospital = BeanUtil.copyProperties(hospitalQueryVo, Hospital.class);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageRequest);
        for (Hospital hosp : pages.getContent()) {
            packHospital(hosp);
        }
        return pages;
    }

    @SneakyThrows
    private void packHospital(Hospital hosp) {
        CompletableFuture<String> hospTypeFuture = CompletableFuture.supplyAsync(() -> {
            Response<String> hospTypeRes = dictFeign.getName(DictEnum.HOSTYPE.getDictCode(), hosp.getHostype());
            return responseConvert.convert(hospTypeRes, new TypeReference<String>() {
            });
        }, executor);
        CompletableFuture<String> provinceCodeFuture = CompletableFuture.supplyAsync(() -> {
            Response<String> provinceCodeRes = dictFeign.getName(null, hosp.getProvinceCode());
            return responseConvert.convert(provinceCodeRes, new TypeReference<String>() {
            });
        }, executor);
        CompletableFuture<String> cityCodeFuture = CompletableFuture.supplyAsync(() -> {
            Response<String> cityCodeRes = dictFeign.getName(null, hosp.getCityCode());
            return responseConvert.convert(cityCodeRes, new TypeReference<String>() {
            });
        }, executor);
        CompletableFuture<String> districtCodeFuture = CompletableFuture.supplyAsync(() -> {
            Response<String> districtCodeRes = dictFeign.getName(null, hosp.getDistrictCode());
            return responseConvert.convert(districtCodeRes, new TypeReference<String>() {
            });
        }, executor);
        CompletableFuture.allOf(hospTypeFuture, cityCodeFuture, provinceCodeFuture, districtCodeFuture);
        hosp.getParam().put("hostypeString", hospTypeFuture.get());
        hosp.getParam().put("fullAddress", provinceCodeFuture.get() + cityCodeFuture.get() + districtCodeFuture.get() + hosp.getAddress());
    }


    public void updateStatus(String id, Integer status) {
        if (status == 0 || status == 1) {
            hospitalRepository.findById(id).ifPresent(hospital -> {
                hospital.setStatus(status);
                hospital.setUpdateTime(new Date());
                hospitalRepository.save(hospital);
            });
        }
    }

    public Map<String, Object> show(String id) {
        Map<String, Object> results = new HashMap<>();
        hospitalRepository.findById(id)
                .ifPresent(hospital -> {
                    this.packHospital(hospital);
                    results.put("hospital",hospital);
                    results.put("bookingRule",hospital.getBookingRule());
                    hospital.setBookingRule(null);
                });
        return results;
    }

    public List<Hospital> findByHosname(String hosname) {
        Hospital hospital = new Hospital();
        hospital.setHosname(hosname);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Hospital> example = Example.of(hospital,exampleMatcher);
        return hospitalRepository.findAll(example);
    }

    public Map<String, Object> item(String hoscode) {
        Map<String, Object> map = new HashMap<>();
        // 医院详情
        Hospital hospital = this.findHospitalByHoscode(hoscode);
        this.packHospital(hospital);
        map.put("hospital",hospital);
        // 预约规则
        map.put("bookingRule",hospital.getBookingRule());
        // 不需要重复返回
        hospital.setBookingRule(null);
        return map;
    }
}
