package org.xiaowu.behappy.cmn.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xiaowu.behappy.api.cmn.entity.Dict;
import org.xiaowu.behappy.api.cmn.vo.DictEeVo;
import org.xiaowu.behappy.cmn.service.DictService;
import org.xiaowu.behappy.common.core.result.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 *
 * @author xiaowu
 */
@Api(tags = "数据字典接口")
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
     * @return org.xiaowu.behappy.common.core.result.Response<java.util.List < org.xiaowu.behappy.api.cmn.entity.Dict>>
     */
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Response<List<Dict>> findChildData(@PathVariable Long id) {
        List<Dict> dictList = dictService.findChildData(id);
        return Response.ok(dictList);
    }

    /**
     * 导出数据字典
     * @apiNote
     * @author xiaowu
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @ApiOperation(value = "导出数据字典")
    @GetMapping("/exportData")
    public Response<Boolean> exportData(HttpServletResponse response) {
        dictService.exportData(response);
        return Response.ok();
    }

    /**
     * 导入数据字典
     * @apiNote name需要指定file, 见https://blog.csdn.net/lingyeran/article/details/122131756
     * @author xiaowu
     * @param multipartFile
     * @return org.xiaowu.behappy.common.core.result.Response<java.lang.Boolean>
     */
    @ApiOperation(value = "导入数据字典")
    @PostMapping("/importData")
    public Response<Boolean> importData(@RequestParam("file") MultipartFile multipartFile) {
        dictService.importData(multipartFile);
        return Response.ok();
    }

}
