package org.xiaowu.behappy.hosp.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaowu.behappy.api.hosp.model.HospitalSet;
import org.xiaowu.behappy.common.core.result.Response;
import org.xiaowu.behappy.hosp.service.HospitalSetService;

import java.util.List;

/**
 * @author xiaowu
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@RequiredArgsConstructor
public class HospitalSetController {

    private final HospitalSetService hospitalSetService;

    /**
     * 查询医院设置表所有信息
     * @apiNote
     * @author xiaowu
     * @return org.xiaowu.behappy.common.core.result.Response<org.xiaowu.behappy.api.hosp.model.HospitalSet>
     */
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("/findAll")
    public Response<List<HospitalSet>> findAllHospSet() {
        // 调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Response.ok(list);
    }

}
