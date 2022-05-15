package org.xiaowu.behappy.cmn.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xiaowu.behappy.api.cmn.entity.Dict;
import org.xiaowu.behappy.api.cmn.vo.DictEeVo;
import org.xiaowu.behappy.cmn.listener.DictListener;
import org.xiaowu.behappy.cmn.mapper.DictMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import static org.xiaowu.behappy.api.cmn.constants.CmnConstant.DICT_CACHE;

/**
 * 数据字典
 * @author xiaowu
 */
@Service
public class DictService extends ServiceImpl<DictMapper, Dict> implements IService<Dict> {

    @Cacheable(cacheNames = DICT_CACHE)
    public List<Dict> findChildData(Long id) {
        LambdaQueryWrapper<Dict> dictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictLambdaQueryWrapper.eq(Dict::getParentId,id);
        List<Dict> dictList = list(dictLambdaQueryWrapper);
        for (Dict dict : dictList) {
            long count = count(new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, dict.getId()));
            if (count > 0){
                dict.setHasChildren(true);
            }else {
                dict.setHasChildren(false);
            }
        }
        return dictList;
    }

    @SneakyThrows
    public void exportData(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据字典", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        List<DictEeVo> dictEeVos = BeanUtil.copyToList(list(), DictEeVo.class, CopyOptions.create());
        EasyExcel.write(response.getOutputStream(),DictEeVo.class)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(fileName)
                .doWrite(dictEeVos);
    }

    /**
     * allEntries = true: 方法调用后清空所有缓存
     * @param multipartFile
     */
    @SneakyThrows
    @CacheEvict(cacheNames = DICT_CACHE)
    public void importData(MultipartFile multipartFile) {
        InputStream inputStream = multipartFile.getInputStream();
        EasyExcel.read(inputStream,DictEeVo.class,new DictListener())
                .excelType(ExcelTypeEnum.XLSX)
                .sheet()
                .doRead();
    }
}
