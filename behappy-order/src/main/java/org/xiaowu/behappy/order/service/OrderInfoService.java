package org.xiaowu.behappy.order.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.order.enums.OrderStatusEnum;
import org.xiaowu.behappy.api.order.vo.OrderCountQueryVo;
import org.xiaowu.behappy.api.order.vo.OrderCountVo;
import org.xiaowu.behappy.api.order.vo.OrderQueryVo;
import org.xiaowu.behappy.api.user.feign.PatientFeign;
import org.xiaowu.behappy.api.user.vo.PatientVo;
import org.xiaowu.behappy.common.core.result.Result;
import org.xiaowu.behappy.common.core.util.ResponseConvert;
import org.xiaowu.behappy.order.entity.OrderInfo;
import org.xiaowu.behappy.order.mapper.OrderInfoMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class OrderInfoService extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IService<OrderInfo> {

    private final PatientFeign patientFeign;

    private final ResponseConvert responseConvert;

    //订单列表（条件查询带分页）
    public IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo) {
        //orderQueryVo获取条件值
        String name = orderQueryVo.getKeyword(); //医院名称
        Long patientId = orderQueryVo.getPatientId(); //就诊人名称
        String orderStatus = orderQueryVo.getOrderStatus(); //订单状态
        String reserveDate = orderQueryVo.getReserveDate();//安排时间
        String createTimeBegin = orderQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderQueryVo.getCreateTimeEnd();
        //对条件值进行非空判断
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if(!StrUtil.isEmpty(name)) {
            wrapper.like("hosname",name);
        }
        if(Objects.nonNull(patientId)) {
            wrapper.eq("patient_id",patientId);
        }
        if(!StrUtil.isEmpty(orderStatus)) {
            wrapper.eq("order_status",orderStatus);
        }
        if(!StrUtil.isEmpty(reserveDate)) {
            wrapper.ge("reserve_date",reserveDate);
        }
        if(!StrUtil.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StrUtil.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }
        // 按更新时间倒序
        wrapper.orderByDesc("update_time");
        //调用mapper的方法
        IPage<OrderInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //编号变成对应值封装
        pages.getRecords().forEach(this::packOrderInfo);
        return pages;
    }

    private OrderInfo packOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put("orderStatusString", OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
        return orderInfo;
    }

    public Map<String, Object> show(Long orderId) {
        Map<String, Object> map = new HashMap<>();
        OrderInfo orderInfo = this.packOrderInfo(this.getById(orderId));
        map.put("orderInfo", orderInfo);
        Result<PatientVo> patientResult = patientFeign.getPatient(orderInfo.getPatientId());
        PatientVo patient = responseConvert.convert(patientResult, new TypeReference<PatientVo>() {
        });
        map.put("patient", patient);
        return map;
    }

    public List<OrderCountVo> selectOrderCount(OrderCountQueryVo orderCountQueryVo) {
        return baseMapper.selectOrderCount(orderCountQueryVo);
    }
}
