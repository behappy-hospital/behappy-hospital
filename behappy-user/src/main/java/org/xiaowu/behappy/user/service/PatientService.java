package org.xiaowu.behappy.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.cmn.feign.DictFeign;
import org.xiaowu.behappy.common.core.enums.DictEnum;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.util.ResponseConvert;
import org.xiaowu.behappy.user.entity.Patient;
import org.xiaowu.behappy.user.mapper.PatientMapper;

import java.util.List;

/**
 *
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class PatientService extends ServiceImpl<PatientMapper, Patient> implements IService<Patient> {

    private final DictFeign dictFeign;

    private final ResponseConvert responseConvert;

    //获取就诊人列表
    public List<Patient> findAllUserId(Long userId) {
        //根据userid查询所有就诊人信息列表
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Patient> patientList = baseMapper.selectList(wrapper);
        //通过远程调用，得到编码对应具体内容，查询数据字典表内容
        patientList.stream().forEach(item -> {
            //其他参数封装
            this.packPatient(item);
        });
        return patientList;
    }

    public Patient getPatientId(Long id) {
        return this.packPatient(baseMapper.selectById(id));
    }

    //Patient对象里面其他参数封装
    private Patient packPatient(Patient patient) {
        //根据证件类型编码，获取证件类型具体指
        Result<String> certificatesTypeRes = dictFeign.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        String certificatesTypeString = responseConvert.convert(certificatesTypeRes, new TypeReference<String>() {
        });
        //联系人证件类型
        Result<String> contactsCertificatesTypeRes = dictFeign.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());
        String contactsCertificatesTypeString = responseConvert.convert(contactsCertificatesTypeRes, new TypeReference<String>() {
        });
        //省
        Result<String> provinceRes = dictFeign.getName(null, patient.getProvinceCode());
        String provinceString = responseConvert.convert(provinceRes, new TypeReference<String>() {
        });
        //市
        Result<String> cityRes = dictFeign.getName(null, patient.getCityCode());
        String cityString = responseConvert.convert(cityRes, new TypeReference<String>() {
        });
        //区
        Result<String> districtRes = dictFeign.getName(null, patient.getDistrictCode());
        String districtString = responseConvert.convert(districtRes, new TypeReference<String>() {
        });
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;
    }

}
