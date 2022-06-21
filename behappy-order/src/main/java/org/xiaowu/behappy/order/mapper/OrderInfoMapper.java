package org.xiaowu.behappy.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xiaowu.behappy.api.order.vo.OrderCountQueryVo;
import org.xiaowu.behappy.api.order.vo.OrderCountVo;
import org.xiaowu.behappy.order.entity.OrderInfo;

import java.util.List;

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo orderCountQueryVo);
}
