package org.xiaowu.behappy.cmn.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.xiaowu.behappy.api.cmn.vo.DictEeVo;
import org.xiaowu.behappy.cmn.entity.Dict;
import org.xiaowu.behappy.cmn.service.DictService;

/**
 * easyexcel listener
 * @author xiaowu
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {

    /**
     * 解析收过来的excel
     * @param dictEeVo
     * @param analysisContext
     */
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        DictService dictService = SpringUtil.getBean(DictService.class);
        Dict dict = BeanUtil.copyProperties(dictEeVo, Dict.class);
        dictService.save(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
