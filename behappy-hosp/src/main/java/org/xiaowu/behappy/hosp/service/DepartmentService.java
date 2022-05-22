package org.xiaowu.behappy.hosp.service;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.hosp.model.Department;
import org.xiaowu.behappy.api.hosp.vo.DepartmentQueryVo;
import org.xiaowu.behappy.hosp.repository.DepartmentRepository;

import java.util.Date;
import java.util.Map;

/**
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void saveDepartment(Map<String, Object> parameterMap) {
        String str = objectMapper.writeValueAsString(parameterMap);
        Department department = objectMapper.readValue(str, Department.class);
        String hoscode = (String) parameterMap.get("hoscode");
        String decode = (String) parameterMap.get("depcode");
        Department targetDepartment = departmentRepository.findDepartmentByHoscodeAndDepcode(hoscode, decode);
        if (targetDepartment != null) {
            // 如果不等于null, 则更新
            BeanUtil.copyProperties(department,targetDepartment);
            departmentRepository.save(targetDepartment);
        }else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    public Page<Department> selectPage(int pageInt, int limitInt, DepartmentQueryVo departmentQueryVo) {
        // 0 为第一页
        PageRequest pageRequest = PageRequest.of(pageInt-1, limitInt, Sort.by(Sort.Direction.DESC, "createTime"));
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
        return departmentRepository.deleteDepartmentByHoscodeAndDepcode(hoscode,depcode);
    }
}
