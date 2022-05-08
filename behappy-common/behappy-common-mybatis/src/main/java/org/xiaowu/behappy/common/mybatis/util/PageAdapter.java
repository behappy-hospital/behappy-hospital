package org.xiaowu.behappy.common.mybatis.util;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author 小五
 * 一对多，多对多分页
 * 使用分页时，前端传入的数据统一格式为current当前页，size每页大小。
 * 而我们在数据库中要将这两个数据变更为从第几行到第几行，所以我们需要简单的适配一下：
 * <p>
 * 在使用mybatis plus 进行分页的时候，该工具会自动为我们编写count的sql，而一对多进行分页时如：
 * 1个订单有5个订单项，在使用mybatis plus 生成的count sql 会认为每行都是一条数据，
 * 导致最后认为会有5条订单信息，实际上应该只有1条订单信息。这个时候我们必须自己手写count sql，
 * 并区分records sql
 * <p>
 * records：LIMIT #{adapter.begin} , #{adapter.size}
 * count: count(0)
 */
@Data
public class PageAdapter {

    private int begin;

    private int size;

    /**
     * int[] startEnd1 = PageUtil.transToStartEnd(1, 10);//[0, 10]
     * int[] startEnd2 = PageUtil.transToStartEnd(2, 10);//[10, 20]
     * @apiNote
     * @author xiaowu
     * @param page
     * @return null
     */
    public PageAdapter(Page page) {
        // 配置1为首页
        PageUtil.setOneAsFirstPageNo();
        int[] startEnd = PageUtil.transToStartEnd((int) page.getCurrent(), (int) page.getSize());
        begin = startEnd[0];
        size = startEnd[1];
    }
}