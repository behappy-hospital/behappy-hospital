package org.xiaowu.behappy.hosp.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xiaowu.behappy.api.hosp.vo.HospitalSetQueryVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.util.MD5;
import org.xiaowu.behappy.hosp.entity.HospitalSet;
import org.xiaowu.behappy.hosp.service.HospitalSetService;

import java.util.List;

/**
 * @author xiaowu
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@RequiredArgsConstructor
@Tag(name = "医院设置管理")
public class HospitalSetController {

    private final HospitalSetService hospitalSetService;

    /**
     * 查询医院设置表所有信息
     * @apiNote
     * @author xiaowu
     * @return org.xiaowu.behappy.common.core.result.Response<org.xiaowu.behappy.hosp.entity.HospitalSet>
     */
    @Operation(summary = "获取所有医院设置")
    @GetMapping("/findAll")
    public Result<List<HospitalSet>> findAllHospSet() {
        // 调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 逻辑删除医院设置
     * @apiNote
     * @author xiaowu
     * @param id
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @Operation(summary = "逻辑删除医院配置")
    @DeleteMapping("/{id}")
    public Result<Boolean> removeById(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return Result.ok(flag);
    }

    /**
     * 条件查询带分页
     * @apiNote
     * @author xiaowu
     * @param current
     * @param limit
     * @param hospitalSetQueryVo
     * @return org.xiaowu.behappy.common.core.result.Response<com.baomidou.mybatisplus.extension.plugins.pagination.Page < org.xiaowu.behappy.hosp.entity.HospitalSet>>
     */
    @Operation(summary = "条件查询带分页")
    @PostMapping("/findPageHospSet/{current}/{limit}")
    public Result<Page<HospitalSet>> info(@PathVariable Integer current,
                                          @PathVariable Integer limit,
                                          @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        // 构建条件
        LambdaQueryWrapper<HospitalSet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(
                StrUtil.isNotEmpty(hospitalSetQueryVo.getHosname()),
                HospitalSet::getHosname,
                hospitalSetQueryVo.getHosname());
        queryWrapper.eq(
                StrUtil.isNotEmpty(hospitalSetQueryVo.getHoscode()),
                HospitalSet::getHoscode,
                hospitalSetQueryVo.getHoscode());
        // 调用方法实现分页查询
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, queryWrapper);
        // 返回结果
        return Result.ok(hospitalSetPage);
    }

    /**
     * 添加医院设置
     * @apiNote
     * @author xiaowu
     * @param hospitalSet
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @Operation(summary = "添加医院设置")
    @PostMapping("/saveHospitalSet")
    public Result<Boolean> saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 设置状态 1:可使用  2:不可使用
        hospitalSet.setStatus(1);
        // 签名密钥
        int randomInt = RandomUtil.randomInt(1_000);
        String encrypt = MD5.encrypt(System.currentTimeMillis() + "" + randomInt);
        hospitalSet.setSignKey(encrypt);
        // 调用service
        boolean save = hospitalSetService.save(hospitalSet);
        return Result.ok(save);
    }

    /**
     * 根据id获取医院设置
     * @apiNote
     * @author xiaowu
     * @param id
     * @return org.xiaowu.behappy.common.core.result.Response<org.xiaowu.behappy.hosp.entity.HospitalSet>
     */
    @Operation(summary = "根据id获取医院设置")
    @GetMapping("/getHospSet/{id}")
    public Result<HospitalSet> getHospital(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    /**
     * 修改医院设置
     * @apiNote
     * @author xiaowu
     * @param hospitalSet
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @Operation(summary = "修改医院设置")
    @GetMapping("/updateHospitalSet/{id}")
    public Result<Boolean> updateHospSet(@RequestBody HospitalSet hospitalSet) {
        boolean update = hospitalSetService.updateById(hospitalSet);
        return Result.ok(update);
    }

    /**
     * 批量删除医院设置
     * @apiNote
     * @author xiaowu
     * @param idList
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @Operation(summary = "批量删除医院设置")
    @DeleteMapping("/batchRemove")
    public Result<Boolean> batchRemove(@RequestBody List<Long> idList) {
        boolean batch = hospitalSetService.removeBatchByIds(idList);
        return Result.ok(batch);
    }

    /**
     * 医院设置锁定和解锁
     * @apiNote
     * @author xiaowu
     * @param id
     * @param status
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @Operation(summary = "医院设置锁定和解锁")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result<Boolean> lockHospitalSet(@PathVariable Long id, @PathVariable Integer status) {
        // 根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 设置状态
        hospitalSet.setStatus(status);
        // 更新
        boolean update = hospitalSetService.updateById(hospitalSet);
        return Result.ok(update);
    }

    /**
     * 发送签名密钥
     * @apiNote
     * @author xiaowu
     * @param id
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @Operation(summary = "发送签名密钥")
    @PutMapping("/sendKey/{id}")
    public Result<Boolean> sendKey(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        // todo 发送短信
        return Result.ok();
    }
}
