package org.xiaowu.behappy.cmn.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xiaowu.behappy.api.cmn.vo.DictEeVo;
import org.xiaowu.behappy.cmn.entity.Dict;
import org.xiaowu.behappy.cmn.listener.DictListener;
import org.xiaowu.behappy.cmn.mapper.DictMapper;
import org.xiaowu.behappy.common.core.constants.CommonConstants;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.xiaowu.behappy.api.cmn.constants.CmnConstant.DICT_CACHE;

/**
 * 数据字典
 * @author xiaowu
 */
@Service
public class DictService extends ServiceImpl<DictMapper, Dict> implements IService<Dict> {
    @Cacheable(value = DICT_CACHE, key = "#id")
    public List<Dict> findChildData(Long id) {
        LambdaQueryWrapper<Dict> dictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictLambdaQueryWrapper.eq(Dict::getParentId,id);
        List<Dict> dictList = list(dictLambdaQueryWrapper);
        for (Dict dict : dictList) {
            long count = count(new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, dict.getId()));
            dict.setHasChildren(count > 0);
        }
        return dictList;
    }


    @SneakyThrows
    public void exportData(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CommonConstants.UTF8);
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据字典", StandardCharsets.UTF_8);
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

    public String getNameByParentDictCodeAndValue(String parentDictCode, String value) {
        // parentDictCode可能为空
        // 省市区的value值可以确定唯一
        if (StrUtil.isEmpty(parentDictCode)){
            LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<Dict>().eq(Dict::getValue, value);
            Dict dict = baseMapper.selectOne(queryWrapper);
            if (dict != null){
                return dict.getName();
            }
        }else {
            // 根据dictCode获取parent id
            LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<Dict>()
                    .select(Dict::getId)
                    .eq(Dict::getDictCode, parentDictCode);
            Dict one = baseMapper.selectOne(wrapper);
            if (one != null){
                LambdaQueryWrapper<Dict> dictLambdaQueryWrapper = new LambdaQueryWrapper<Dict>()
                        .eq(Dict::getParentId, one.getId())
                        .eq(Dict::getValue, value);
                Dict selectOne = baseMapper.selectOne(dictLambdaQueryWrapper);
                if (selectOne != null){
                    return selectOne.getName();
                }
            }
        }
        return "";
    }

    public List<Dict> findByDictCode(String dictCode) {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<Dict>()
                .select(Dict::getId)
                .eq(Dict::getDictCode, dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        if (dict != null){
            return findChildData(dict.getId());
        }
        return null;
    }
}
