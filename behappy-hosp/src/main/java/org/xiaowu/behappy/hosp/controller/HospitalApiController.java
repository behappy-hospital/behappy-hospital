package org.xiaowu.behappy.hosp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.hosp.model.Hospital;
import org.xiaowu.behappy.api.hosp.vo.DepartmentVo;
import org.xiaowu.behappy.api.hosp.vo.HospitalQueryVo;
import org.xiaowu.behappy.common.core.result.Response;
import org.xiaowu.behappy.hosp.service.DepartmentService;
import org.xiaowu.behappy.hosp.service.HospitalService;

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

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Response index(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            HospitalQueryVo hospitalQueryVo) {
        //显示上线的医院
        //hospitalQueryVo.setStatus(1);
        Page<Hospital> pageModel = hospitalService.listPage(page, limit, hospitalQueryVo);
        return Response.ok(pageModel);
    }

    @ApiOperation(value = "获取科室列表")
    @GetMapping("/department/{hoscode}")
    public Response<List<DepartmentVo>> departmentList(@PathVariable String hoscode) {
        List<DepartmentVo> deptTree = departmentService.findDeptTree(hoscode);
        return Response.ok(deptTree);
    }

    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("/{hoscode}")
    public Response<Map<String, Object>> item(@PathVariable String hoscode) {
        Map<String, Object> map = hospitalService.item(hoscode);
        return Response.ok(map);
    }


    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("/findByHosname/{hosname}")
    public Response<List<Hospital>> findByHosname(@PathVariable String hosname) {
        List<Hospital> hospitalList = hospitalService.findByHosname(hosname);
        return Response.ok(hospitalList);
    }
}
