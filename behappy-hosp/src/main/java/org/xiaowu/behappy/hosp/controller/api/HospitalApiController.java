package org.xiaowu.behappy.hosp.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.common.vo.SignInfoVo;
import org.xiaowu.behappy.api.hosp.vo.DepartmentVo;
import org.xiaowu.behappy.api.hosp.vo.HospitalQueryVo;
import org.xiaowu.behappy.api.hosp.vo.ScheduleOrderVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.hosp.entity.Hospital;
import org.xiaowu.behappy.hosp.entity.Schedule;
import org.xiaowu.behappy.hosp.service.DepartmentService;
import org.xiaowu.behappy.hosp.service.HospitalService;
import org.xiaowu.behappy.hosp.service.HospitalSetService;
import org.xiaowu.behappy.hosp.service.ScheduleService;

import java.util.List;
import java.util.Map;

/**
 * @author xiaowu
 */
@Tag(name = "医院接口(用户端)")
@RestController
@RequestMapping("/api/hosp/hospital")
@AllArgsConstructor
public class HospitalApiController {

    private final DepartmentService departmentService;

    private final HospitalService hospitalService;

    private final ScheduleService scheduleService;

    private final HospitalSetService hospitalSetService;

    @Operation(summary = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result<Page<Hospital>> index(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            HospitalQueryVo hospitalQueryVo) {
        //todo 显示上线的医院
        //hospitalQueryVo.setStatus(1);
        Page<Hospital> pageModel = hospitalService.listPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    @Operation(summary = "获取科室列表")
    @GetMapping("/department/{hoscode}")
    public Result<List<DepartmentVo>> departmentList(@PathVariable String hoscode) {
        List<DepartmentVo> deptTree = departmentService.findDeptTree(hoscode);
        return Result.ok(deptTree);
    }

    @Operation(summary = "医院预约挂号详情")
    @GetMapping("/{hoscode}")
    public Result<Map<String, Object>> item(
            @Parameter(name = "hoscode", required = true)
            @PathVariable("hoscode") String hoscode) {
        Map<String, Object> map = hospitalService.item(hoscode);
        return Result.ok(map);
    }


    @Operation(summary = "根据医院名称获取医院列表")
    @GetMapping("/findByHosname/{hosname}")
    public Result<List<Hospital>> findByHosname(@PathVariable String hosname) {
        List<Hospital> hospitalList = hospitalService.findByHosname(hosname);
        return Result.ok(hospitalList);
    }

    @Operation(summary = "获取可预约排班数据")
    @GetMapping("/auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result<Map<String, Object>> getBookingSchedule(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Integer limit,
            @Parameter(name = "hoscode", description = "医院code", required = true)
            @PathVariable String hoscode,
            @Parameter(name = "depcode", description = "科室code", required = true)
            @PathVariable String depcode) {
        return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode));
    }

    @Operation(summary = "获取排班数据")
    @GetMapping("/auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result<List<Schedule>> findScheduleList(
            @Parameter(name = "hoscode", description = "医院code", required = true)
            @PathVariable String hoscode,
            @Parameter(name = "depcode", description = "科室code", required = true)
            @PathVariable String depcode,
            @Parameter(name = "workDate", description = "排班日期", required = true)
            @PathVariable String workDate) {
        return Result.ok(scheduleService.getDetailSchedule(hoscode, depcode, workDate));
    }

    @Operation(summary = "根据排班id获取排班数据")
    @GetMapping("getSchedule/{scheduleId}")
    public Result<Schedule> getSchedule(
            @Parameter(name = "scheduleId", description = "排班id", required = true)
            @PathVariable String scheduleId) {
        return Result.ok(scheduleService.getById(scheduleId));
    }

    @Operation(summary = "根据排班id获取预约下单数据")
    @GetMapping("/inner/getScheduleOrderVo/{scheduleId}")
    public Result<ScheduleOrderVo> getScheduleOrderVo(
            @Parameter(name = "scheduleId", description = "排班id", required = true)
            @PathVariable("scheduleId") String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = scheduleService.getScheduleOrderVo(scheduleId);
        return Result.ok(scheduleOrderVo);
    }

    @Operation(summary = "获取医院签名信息")
    @GetMapping("/inner/getSignInfoVo/{hoscode}")
    public Result<SignInfoVo> getSignInfoVo(
            @Parameter(name = "hoscode", description = "医院code", required = true)
            @PathVariable("hoscode") String hoscode) {
        SignInfoVo signInfoVo = hospitalSetService.getSignInfoVo(hoscode);
        return Result.ok(signInfoVo);
    }


}
