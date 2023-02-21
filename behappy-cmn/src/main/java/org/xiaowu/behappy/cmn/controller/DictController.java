package org.xiaowu.behappy.cmn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xiaowu.behappy.cmn.entity.Dict;
import org.xiaowu.behappy.cmn.service.DictService;
import org.xiaowu.behappy.common.core.result.Result;

import java.util.List;

/**
 *
 * @author xiaowu
 */
@Tag(name = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    /**
     * 根据数据id查询子数据列表
     * @apiNote
     * @author xiaowu
     * @param id
     * @return org.xiaowu.behappy.common.core.result.Response<java.util.List < org.xiaowu.behappy.cmn.entity.Dict>>
     */
    @Operation(summary = "根据数据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result<List<Dict>> findChildData(@PathVariable Long id) {
        List<Dict> dictList = dictService.findChildData(id);
        return Result.ok(dictList);
    }

    /**
     * 导出数据字典
     * @apiNote
     * @author xiaowu
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @Operation(summary = "导出数据字典")
    @GetMapping("/exportData")
    public Result<Boolean> exportData(HttpServletResponse response) {
        dictService.exportData(response);
        return Result.ok();
    }

    /**
     * 导入数据字典
     * @apiNote name需要指定file, 见https://blog.csdn.net/lingyeran/article/details/122131756
     * @author xiaowu
     * @param multipartFile
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @Operation(summary = "导入数据字典")
    @PostMapping("/importData")
    public Result<Boolean> importData(@RequestParam("file") MultipartFile multipartFile) {
        dictService.importData(multipartFile);
        return Result.ok();
    }

    /**
     * 数据字典名称
     * @apiNote
     * @author xiaowu
     * @param parentDictCode
     * @param value
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.String>
     */
    @Operation(summary = "数据字典名称")
    @GetMapping("/getName")
    public Result<String> getName(@RequestParam(value = "parentDictCode", required = false) String parentDictCode,
                                  @RequestParam(value = "value") String value) {
        String dictName = dictService.getNameByParentDictCodeAndValue(parentDictCode, value);
        return Result.ok(dictName);
    }

    @Operation(summary = "根据dictCode获取下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(@PathVariable String dictCode) {
        List<Dict> dictList = dictService.findByDictCode(dictCode);
        return Result.ok(dictList);
    }

}
