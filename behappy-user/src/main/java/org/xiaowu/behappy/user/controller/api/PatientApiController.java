package org.xiaowu.behappy.user.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.util.AuthContextHolder;
import org.xiaowu.behappy.user.entity.Patient;
import org.xiaowu.behappy.user.service.PatientService;

import java.util.List;

//就诊人管理接口
@RestController
@RequestMapping("/api/user/patient")
@RequiredArgsConstructor
public class PatientApiController {
    private final PatientService patientService;

    //获取就诊人列表
    @GetMapping("/auth/findAll")
    public Result<List<Patient>> findAll(HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllUserId(userId);
        return Result.ok(list);
    }
    //添加就诊人
    @PostMapping("/auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }
    //根据id获取就诊人信息
    @GetMapping("/auth/get/{id}")
    public Result<Patient> getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }
    //修改就诊人
    @PostMapping("/auth/update")
    public Result updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }
    //删除就诊人
    @DeleteMapping("/auth/remove/{id}")
    public Result removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.ok();
    }

    /**
     * 获取就诊人
     * @param id
     * @return
     */
    @Operation(summary = "获取就诊人")
    @GetMapping("/inner/get/{id}")
    public Result<Patient> getPatientOrder(
            @Parameter(name = "id", description = "就诊人id", required = true)
            @PathVariable("id") Long id) {
        Patient patient = patientService.getById(id);
        return Result.ok(patient);
    }
}
