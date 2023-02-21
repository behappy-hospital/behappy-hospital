package org.xiaowu.behappy.hosp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.hosp.entity.Schedule;
import org.xiaowu.behappy.hosp.service.ScheduleService;

import java.util.List;
import java.util.Map;

/**
 * 排班接口
 * @author xiaowu
 */
@Tag(name = "排班接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 根据医院编号 和 科室编号 ，查询排班规则数据
     * @apiNote
     * @author xiaowu
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return org.xiaowu.behappy.common.core.result.Response<java.util.Map < java.lang.String, java.lang.Object>>
     */
    @Operation(summary = "根据医院编号和科室编号，查询排班规则数据")
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result<Map<String, Object>> getScheduleRule(@PathVariable Long page,
                                                       @PathVariable Long limit,
                                                       @PathVariable String hoscode,
                                                       @PathVariable String depcode) {
        Map<String, Object> map = scheduleService.getScheduleRule(page, limit, hoscode, depcode);
        return Result.ok(map);
    }

    /**
     * 根据医院编号 、科室编号和工作日期，查询排班详细信息
     * @apiNote
     * @author xiaowu
     * @param hoscode
     * @param depcode
     * @param workDate
     * @return org.xiaowu.behappy.common.core.result.Response<java.util.List < org.xiaowu.behappy.hosp.entity.Schedule>>
     */
    @Operation(summary = "根据医院编号 、科室编号和工作日期，查询排班详细信息")
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result<List<Schedule>> getScheduleDetail(@PathVariable String hoscode,
                                                    @PathVariable String depcode,
                                                    @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode, depcode, workDate);
        return Result.ok(list);
    }



}
