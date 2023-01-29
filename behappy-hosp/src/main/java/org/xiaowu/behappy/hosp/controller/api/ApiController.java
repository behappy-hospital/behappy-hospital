package org.xiaowu.behappy.hosp.controller.api;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.hosp.vo.DepartmentQueryVo;
import org.xiaowu.behappy.api.hosp.vo.ScheduleQueryVo;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.common.core.util.HttpRequestHelper;
import org.xiaowu.behappy.hosp.entity.Department;
import org.xiaowu.behappy.hosp.entity.Hospital;
import org.xiaowu.behappy.hosp.entity.Schedule;
import org.xiaowu.behappy.hosp.service.DepartmentService;
import org.xiaowu.behappy.hosp.service.HospitalService;
import org.xiaowu.behappy.hosp.service.HospitalSetService;
import org.xiaowu.behappy.hosp.service.ScheduleService;

import java.util.Map;

/**
 * @author xiaowu
 */
@Tag(name = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
@AllArgsConstructor
public class ApiController {
    private final HospitalService hospitalService;

    private final HospitalSetService hospitalSetService;

    private final DepartmentService departmentService;

    private final ScheduleService scheduleService;

    /**
     * 删除排班
     * @apiNote
     * @author xiaowu
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @PostMapping("/schedule/remove")
    public Result<Boolean> removeSchedule(HttpServletRequest request) {
        Map<String, Object> parameterMap = getParameterMap(request);
        // 签名校验
        checkSign(parameterMap);
        return Result.ok(scheduleService.removeSchedule(parameterMap) > 0);
    }


    /**
     * 排班分页
     * @apiNote
     * @author xiaowu
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response<org.springframework.data.domain.Page < org.xiaowu.behappy.hosp.entity.Schedule>>
     */
    @PostMapping("/schedule/list")
    public Result<Page<Schedule>> scheduleList(HttpServletRequest request) {
        Map<String, Object> parameterMap = getParameterMap(request);
        // 签名校验
        checkSign(parameterMap);
        String hoscode = (String) parameterMap.get("hoscode");
        String depcode = (String) parameterMap.get("depcode");
        String page = (String) parameterMap.get("page");
        String limit = (String) parameterMap.get("limit");
        // 默认值
        int pageInt = StrUtil.isEmpty(page) ? 1 : Integer.parseInt(page);
        int limitInt = StrUtil.isEmpty(limit) ? 10 : Integer.parseInt(limit);
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> schedulePage = scheduleService.selectPage(pageInt, limitInt, scheduleQueryVo);
        return Result.ok(schedulePage);
    }

    /**
     * 上传排班
     * @apiNote
     * @author xiaowu
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @PostMapping("/saveSchedule")
    public Result<Boolean> saveSchedule(HttpServletRequest request) {
        Map<String, Object> parameterMap = getParameterMap(request);
        // 签名校验
        checkSign(parameterMap);
        scheduleService.saveSchedule(parameterMap);
        return Result.ok(Boolean.TRUE);
    }

    /**
     * 删除科室
     * @apiNote
     * @author xiaowu
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @PostMapping("/department/remove")
    public Result<Boolean> removeDep(HttpServletRequest request) {
        Map<String, Object> parameterMap = getParameterMap(request);
        // 签名校验
        checkSign(parameterMap);
        String hoscode = (String) parameterMap.get("hoscode");
        String depcode = (String) parameterMap.get("depcode");
        return Result.ok(departmentService.removeDep(hoscode, depcode) > 0);
    }

    /**
     * 分页查询科室
     * @apiNote
     * @author xiaowu
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response<org.springframework.data.domain.Page < org.xiaowu.behappy.hosp.entity.Department>>
     */
    @PostMapping("/department/list")
    public Result<Page<Department>> departmentListPage(HttpServletRequest request) {
        Map<String, Object> parameterMap = getParameterMap(request);
        String hoscode = (String) parameterMap.get("hoscode");
        // 签名校验
        checkSign(parameterMap);
        String depcode = ((String) parameterMap.get("depcode"));
        String page = (String) parameterMap.get("page");
        String limit = (String) parameterMap.get("limit");
        // 默认值
        int pageInt = StrUtil.isEmpty(page) ? 1 : Integer.parseInt(page);
        int limitInt = StrUtil.isEmpty(limit) ? 10 : Integer.parseInt(limit);
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        departmentQueryVo.setDepcode(depcode);
        Page<Department> depPage = departmentService.selectPage(pageInt, limitInt, departmentQueryVo);
        return Result.ok(depPage);
    }

    /**
     * 上传科室
     * @apiNote
     * @author xiaowu
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @PostMapping("/saveDepartment")
    public Result<Boolean> saveDepartment(HttpServletRequest request) {
        Map<String, Object> parameterMap = getParameterMap(request);
        // 签名校验
        checkSign(parameterMap);
        departmentService.saveDepartment(parameterMap);
        return Result.ok(Boolean.TRUE);
    }

    /**
     * 查看医院
     * @apiNote
     * @author xiaowu
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response<org.xiaowu.behappy.hosp.entity.Hospital>
     */
    @PostMapping("/hospital/show")
    public Result<Hospital> hospital(HttpServletRequest request) {
        Map<String, Object> parameterMap = getParameterMap(request);
        String hoscode = (String) parameterMap.get("hoscode");
        // 签名校验
        checkSign(parameterMap);
        return Result.ok(hospitalService.findHospitalByHoscode(hoscode));
    }

    /**
     * 上传医院
     * @apiNote
     * @author xiaowu
     * @param request
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @PostMapping("/saveHospital")
    public Result<Boolean> saveHosp(HttpServletRequest request) {
        Map<String, Object> parameterMap = getParameterMap(request);
        // base64 空格转换
        extractLogData(parameterMap);
        hospitalService.saveHosp(parameterMap);
        return Result.ok(true);
    }

    private Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> parameterMap = HttpRequestHelper.switchMap(map);
        String hoscode = (String) parameterMap.get("hoscode");
        if (StrUtil.isEmpty(hoscode)) {
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }
        return parameterMap;
    }

    private void checkSign(Map<String, Object> parameterMap) {
        extractLogData(parameterMap);
        // 签名校验
        String hoscode = (String) parameterMap.get("hoscode");
        if (!HttpRequestHelper.isSignEquals(parameterMap, hospitalSetService.getSignKey(hoscode))) {
            throw new HospitalException(ResultCodeEnum.SIGN_ERROR);
        }
    }

    private void extractLogData(Map<String, Object> parameterMap) {
        // base64码通过http传输 +号变 空格
        String logoData = (String) parameterMap.get("logoData");
        if (StrUtil.isNotEmpty(logoData)) {
            logoData = logoData.replaceAll(" ", "+");
            parameterMap.put("logoData", logoData);
        }
    }
}
