package org.xiaowu.behappy.hosp.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.hosp.model.Schedule;
import org.xiaowu.behappy.api.hosp.vo.ScheduleQueryVo;
import org.xiaowu.behappy.hosp.repository.ScheduleRepository;

import java.util.Date;
import java.util.Map;

/**
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

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
}