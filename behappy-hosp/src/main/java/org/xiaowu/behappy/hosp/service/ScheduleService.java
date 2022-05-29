package org.xiaowu.behappy.hosp.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.hosp.model.Schedule;
import org.xiaowu.behappy.api.hosp.vo.BookingScheduleRuleVo;
import org.xiaowu.behappy.api.hosp.vo.ScheduleQueryVo;
import org.xiaowu.behappy.common.core.util.JodaTimeUtils;
import org.xiaowu.behappy.hosp.repository.DepartmentRepository;
import org.xiaowu.behappy.hosp.repository.ScheduleRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final HospitalService hospitalService;

    private final DepartmentRepository departmentRepository;

    private final MongoTemplate mongoTemplate;

    public void saveSchedule(Map<String, Object> parameterMap) {
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(parameterMap), Schedule.class);
        Schedule targetSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (null != targetSchedule) {
            BeanUtil.copyProperties(schedule, targetSchedule);
            scheduleRepository.save(targetSchedule);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);

        }
    }

    public Page<Schedule> selectPage(int pageInt, int limitInt, ScheduleQueryVo scheduleQueryVo) {
        PageRequest pageRequest = PageRequest.of(pageInt - 1, limitInt, Sort.by(Sort.Direction.DESC, "createTime"));
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        Schedule schedule = BeanUtil.copyProperties(scheduleQueryVo, Schedule.class);
        Example<Schedule> scheduleExample = Example.of(schedule, exampleMatcher);
        Page<Schedule> all = scheduleRepository.findAll(scheduleExample, pageRequest);
        return all;
    }

    public int removeSchedule(Map<String, Object> paramMap) {
        String hoscode = (String)paramMap.get("hoscode");
        String hosScheduleId = (String)paramMap.get("hosScheduleId");
        return scheduleRepository.deleteScheduleByHoscodeAndHosScheduleId(hoscode,hosScheduleId);
    }

    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        // 根据参数查询mongodb
        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        // 把得到list集合便利, 设置其他值: 医院名称, 科室名称, 日期对应星期
        scheduleList.stream().forEach(schedule -> this.packageSchedule(schedule));
        return scheduleList;
    }


    public Map<String, Object> getScheduleRule(Long page, Long limit, String hoscode, String depcode) {
        // 根据医院编号和科室编号查询
        Criteria criteria = Criteria.where("hoscode")
                .is(hoscode)
                .and("depcode")
                .is(depcode);
        // 根据工作日workDate进行分组
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),// 匹配条件
                Aggregation.group("workDate")// 分组字段
                        .first("workDate").as("workDate")
                        // 统计号源数量
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                // 排序
                Aggregation.sort(Sort.Direction.DESC, "workDate"),
                // 分页
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)

        );
        // 调用方法,最终执行
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVos = aggregationResults.getMappedResults();
        // 分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggregate = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggregate.getMappedResults().size();
        // 获取日期对应的星期
        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVos) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = JodaTimeUtils.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }
        // 设置最终数据,进行返回
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList", bookingScheduleRuleVos);
        result.put("total", total);
        // 获取医院名称
        String hosname = hospitalService.findHospitalByHoscode(hoscode).getHosname();
        // 其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname",hosname);
        result.put("baseMap",baseMap);
        return result;
    }

    private void packageSchedule(Schedule schedule) {
        // 医院名称
        String hosname = hospitalService.findHospitalByHoscode(schedule.getHoscode()).getHosname();
        schedule.getParam().put("hosname", hosname);
        // 科室名称
        String depname = departmentRepository.findDepartmentByHoscodeAndDepcode(schedule.getHoscode(), schedule.getDepcode()).getDepname();
        schedule.getParam().put("depname",depname);
        // 设置日期对应星期
        schedule.getParam().put("dayOfWeek", JodaTimeUtils.getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }
}