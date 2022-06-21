package org.xiaowu.behappy.hosp.service;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.hosp.vo.DepartmentQueryVo;
import org.xiaowu.behappy.api.hosp.vo.DepartmentVo;
import org.xiaowu.behappy.hosp.entity.Department;
import org.xiaowu.behappy.hosp.repository.DepartmentRepository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final ObjectMapper objectMapper;

    private final MongoTemplate mongoTemplate;

    private final HospitalService hospitalService;

    @SneakyThrows
    public void saveDepartment(Map<String, Object> parameterMap) {
        String str = objectMapper.writeValueAsString(parameterMap);
        Department department = objectMapper.readValue(str, Department.class);
        String hoscode = (String) parameterMap.get("hoscode");
        String decode = (String) parameterMap.get("depcode");
        Department targetDepartment = getDepartmentByHoscodeAndDepcode(hoscode, decode);
        if (targetDepartment != null) {
            // 如果不等于null, 则更新
            BeanUtil.copyProperties(department, targetDepartment);
            departmentRepository.save(targetDepartment);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    public Department getDepartmentByHoscodeAndDepcode(String hoscode, String decode) {
        return departmentRepository.findDepartmentByHoscodeAndDepcode(hoscode, decode);
    }

    public Page<Department> selectPage(int pageInt, int limitInt, DepartmentQueryVo departmentQueryVo) {
        // 0 为第一页
        PageRequest pageRequest = PageRequest.of(pageInt - 1, limitInt, Sort.by(Sort.Direction.DESC, "createTime"));
        Department department = BeanUtil.copyProperties(departmentQueryVo, Department.class);
        department.setIsDeleted(0);
        // 创建匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        Example<Department> departmentExample = Example.of(department, exampleMatcher);
        Page<Department> all = departmentRepository.findAll(departmentExample, pageRequest);
        return all;
    }

    public int removeDep(String hoscode, String depcode) {
        return departmentRepository.deleteDepartmentByHoscodeAndDepcode(hoscode, depcode);
    }

    public List<DepartmentVo> findDeptTree(String hoscode) {
        // 用于最终数据封装
        List<DepartmentVo> departmentVoResults = new LinkedList<>();
        // 根据医院编号查询医院所有科室信息
        Example<Department> exampleQuery = Example.of(new Department()
                .setHoscode(hoscode));
        List<Department> departmentListByHoscode = departmentRepository.findAll(exampleQuery);
        // 根据大科室编号bigcode分组, 获取每个大科室下的下级子科室
        Map<String, List<Department>> departmentMap = departmentListByHoscode.stream()
                .collect(Collectors.groupingBy(Department::getBigcode));
        for (Map.Entry<String, List<Department>> departmentEntry : departmentMap.entrySet()) {
            // 大科室编号
            String bigcode = departmentEntry.getKey();
            // 大科室编号对应的全局数据
            List<Department> departmentList = departmentEntry.getValue();
            // 封装大科室
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigcode);
            departmentVo.setDepname(departmentList.get(0).getDepname());
            // 封装小科室
            List<DepartmentVo> childList = new LinkedList<>();
            for (Department department : departmentList) {
                DepartmentVo childDepartment = new DepartmentVo();
                childDepartment.setDepcode(department.getDepcode());
                childDepartment.setDepname(department.getDepname());
                // 封装childList
                childList.add(childDepartment);
            }
            // 把小科室list放在大科室的children里面
            departmentVo.setChildren(childList);
            // 在放在最终的result里面
            departmentVoResults.add(departmentVo);
        }
        return departmentVoResults;
    }

}
