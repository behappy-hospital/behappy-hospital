package org.xiaowu.behappy.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.api.user.model.Patient;
import org.xiaowu.behappy.common.core.result.Response;
import org.xiaowu.behappy.common.core.util.AuthContextHolder;
import org.xiaowu.behappy.user.service.PatientService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//就诊人管理接口
@RestController
@RequestMapping("/api/user/patient")
@RequiredArgsConstructor
public class PatientApiController {
    private final PatientService patientService;

    //获取就诊人列表
    @GetMapping("/auth/findAll")
    public Response<List<Patient>> findAll(HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllUserId(userId);
        return Response.ok(list);
    }
    //添加就诊人
    @PostMapping("/auth/save")
    public Response savePatient(@RequestBody Patient patient,HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Response.ok();
    }
    //根据id获取就诊人信息
    @GetMapping("/auth/get/{id}")
    public Response<Patient> getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return Response.ok(patient);
    }
    //修改就诊人
    @PostMapping("/auth/update")
    public Response updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Response.ok();
    }
    //删除就诊人
    @DeleteMapping("/auth/remove/{id}")
    public Response removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Response.ok();
    }
}
