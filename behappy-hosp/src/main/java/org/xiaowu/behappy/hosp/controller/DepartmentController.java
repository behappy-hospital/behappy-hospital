package org.xiaowu.behappy.hosp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.hosp.vo.DepartmentVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.hosp.service.DepartmentService;

import java.util.List;

/**
 * 科室接口
 * @author xiaowu
 */
@Tag(name = "科室接口")
@RestController
@RequestMapping("/admin/hosp/department")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 根据医院编号，查询医院所有科室列表
     * @apiNote
     * @author xiaowu
     * @param hoscode
     * @return org.xiaowu.behappy.common.core.result.Response<java.util.List < org.xiaowu.behappy.api.hosp.vo.DepartmentVo>>
     */
    @Operation(summary = "根据医院编号，查询医院所有科室列表")
    @GetMapping("/getDeptList/{hoscode}")
    public Result<List<DepartmentVo>> getDeptList(@PathVariable String hoscode) {
        List<DepartmentVo> departmentVos = departmentService.findDeptTree(hoscode);
        return Result.ok(departmentVos);
    }

}
