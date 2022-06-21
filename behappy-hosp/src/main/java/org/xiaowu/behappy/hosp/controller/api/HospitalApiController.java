package org.xiaowu.behappy.hosp.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "医院接口(用户端)")
@RestController
@RequestMapping("/api/hosp/hospital")
@AllArgsConstructor
public class HospitalApiController {

    private final DepartmentService departmentService;

    private final HospitalService hospitalService;

    private final ScheduleService scheduleService;

    private final HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            HospitalQueryVo hospitalQueryVo) {
        //显示上线的医院
        //hospitalQueryVo.setStatus(1);
        Page<Hospital> pageModel = hospitalService.listPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取科室列表")
    @GetMapping("/department/{hoscode}")
    public Result<List<DepartmentVo>> departmentList(@PathVariable String hoscode) {
        List<DepartmentVo> deptTree = departmentService.findDeptTree(hoscode);
        return Result.ok(deptTree);
    }

    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("/{hoscode}")
    public Result<Map<String, Object>> item(@PathVariable String hoscode) {
        Map<String, Object> map = hospitalService.item(hoscode);
        return Result.ok(map);
    }


    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("/findByHosname/{hosname}")
    public Result<List<Hospital>> findByHosname(@PathVariable String hosname) {
        List<Hospital> hospitalList = hospitalService.findByHosname(hosname);
        return Result.ok(hospitalList);
    }

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("/auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result<Map<String, Object>> getBookingSchedule(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode) {
        return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode));
    }

    @ApiOperation(value = "获取排班数据")
    @GetMapping("/auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result<List<Schedule>> findScheduleList(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode,
            @ApiParam(name = "workDate", value = "排班日期", required = true)
            @PathVariable String workDate) {
        return Result.ok(scheduleService.getDetailSchedule(hoscode, depcode, workDate));
    }

    @ApiOperation(value = "根据排班id获取排班数据")
    @GetMapping("getSchedule/{scheduleId}")
    public Result<Schedule> getSchedule(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId) {
        return Result.ok(scheduleService.getById(scheduleId));
    }

    @ApiOperation(value = "根据排班id获取预约下单数据")
    @GetMapping("/inner/getScheduleOrderVo/{scheduleId}")
    public Result<ScheduleOrderVo> getScheduleOrderVo(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable("scheduleId") String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = scheduleService.getScheduleOrderVo(scheduleId);
        return Result.ok(scheduleOrderVo);
    }

    @ApiOperation(value = "获取医院签名信息")
    @GetMapping("/inner/getSignInfoVo/{hoscode}")
    public Result<SignInfoVo> getSignInfoVo(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable("hoscode") String hoscode) {
        SignInfoVo signInfoVo = hospitalSetService.getSignInfoVo(hoscode);
        return Result.ok(signInfoVo);
    }


}
