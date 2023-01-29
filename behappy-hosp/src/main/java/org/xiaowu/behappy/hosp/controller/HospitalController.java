package org.xiaowu.behappy.hosp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.hosp.vo.HospitalQueryVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.hosp.entity.Hospital;
import org.xiaowu.behappy.hosp.service.HospitalService;

import java.util.Map;

/**
 * 医院管理接口
 * @author xiaowu
 */
@Tag(name = "医院管理接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
@AllArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    /**
     * 获取分页列表
     * @apiNote
     * @author xiaowu
     * @param page
     * @param limit
     * @param hospitalQueryVo
     * @return org.xiaowu.behappy.common.core.result.Response<org.springframework.data.domain.Page < org.xiaowu.behappy.hosp.entity.Hospital>>
     */
    @Operation(summary = "获取分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result<Page<Hospital>> listPage(@Parameter(description = "当前页码", required = true) @PathVariable Integer page,
                                           @Parameter(description = "每页记录数", required = true) @PathVariable Integer limit,
                                           HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageRes = hospitalService.listPage(page, limit, hospitalQueryVo);
        return Result.ok(pageRes);
    }

    /**
     * 更新上线状态
     * @apiNote
     * @author xiaowu
     * @param id
     * @param status
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @Operation(summary = "更新上线状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result<Boolean> lock(
            @Parameter(name = "id", description = "医院id", required = true)
            @PathVariable("id") String id,
            @Parameter(name = "status", description = "状态（0：未上线 1：已上线）", required = true)
            @PathVariable("status") Integer status) {
        hospitalService.updateStatus(id, status);
        return Result.ok(true);
    }

    /**
     * 获取医院详情
     * @apiNote
     * @author xiaowu
     * @param id
     * @return org.xiaowu.behappy.common.core.result.Response<org.xiaowu.behappy.hosp.entity.Hospital>
     */
    @Operation(summary = "获取医院详情")
    @GetMapping("/showHospDetail/{id}")
    public Result<Map<String, Object>> show(
            @Parameter(name = "id", description = "医院id", required = true)
            @PathVariable String id) {
        Map<String, Object> objectMap = hospitalService.show(id);
        return Result.ok(objectMap);
    }


}
