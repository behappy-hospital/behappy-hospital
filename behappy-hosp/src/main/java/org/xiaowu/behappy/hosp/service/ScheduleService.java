package org.xiaowu.behappy.hosp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.xiaowu.behappy.api.hosp.vo.BookingScheduleRuleVo;
import org.xiaowu.behappy.api.hosp.vo.ScheduleOrderVo;
import org.xiaowu.behappy.api.hosp.vo.ScheduleQueryVo;
import org.xiaowu.behappy.common.core.exception.HospitalException;
import org.xiaowu.behappy.common.core.result.ResultCodeEnum;
import org.xiaowu.behappy.common.core.util.JodaTimeUtils;
import org.xiaowu.behappy.hosp.entity.BookingRule;
import org.xiaowu.behappy.hosp.entity.Department;
import org.xiaowu.behappy.hosp.entity.Hospital;
import org.xiaowu.behappy.hosp.entity.Schedule;
import org.xiaowu.behappy.hosp.repository.DepartmentRepository;
import org.xiaowu.behappy.hosp.repository.ScheduleRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaowu
 */
@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final HospitalService hospitalService;

    private final DepartmentRepository departmentRepository;

    private final DepartmentService departmentService;

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
        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        return scheduleRepository.deleteScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
    }

    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        // ??????????????????mongodb
        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        // ?????????list????????????, ???????????????: ????????????, ????????????, ??????????????????
        scheduleList.stream().forEach(schedule -> this.packageSchedule(schedule));
        return scheduleList;
    }


    public Map<String, Object> getScheduleRule(Long page, Long limit, String hoscode, String depcode) {
        // ???????????????????????????????????????
        Criteria criteria = Criteria.where("hoscode")
                .is(hoscode)
                .and("depcode")
                .is(depcode);
        // ???????????????workDate????????????
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),// ????????????
                Aggregation.group("workDate")// ????????????
                        .first("workDate").as("workDate")
                        // ??????????????????
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                // ??????
                Aggregation.sort(Sort.Direction.DESC, "workDate"),
                // ??????
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)

        );
        // ????????????,????????????
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVos = aggregationResults.getMappedResults();
        // ???????????????????????????
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggregate = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggregate.getMappedResults().size();
        // ???????????????????????????
        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVos) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = JodaTimeUtils.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }
        // ??????????????????,????????????
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList", bookingScheduleRuleVos);
        result.put("total", total);
        // ??????????????????
        String hosname = hospitalService.findHospitalByHoscode(hoscode).getHosname();
        // ??????????????????
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hosname);
        result.put("baseMap", baseMap);
        return result;
    }

    private void packageSchedule(Schedule schedule) {
        // ????????????
        String hosname = hospitalService.findHospitalByHoscode(schedule.getHoscode()).getHosname();
        schedule.getParam().put("hosname", hosname);
        // ????????????
        String depname = departmentRepository.findDepartmentByHoscodeAndDepcode(schedule.getHoscode(), schedule.getDepcode()).getDepname();
        schedule.getParam().put("depname", depname);
        // ????????????????????????
        schedule.getParam().put("dayOfWeek", JodaTimeUtils.getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }

    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        Map<String, Object> result = new HashMap<>();
        // ??????????????????
        Hospital dbHospital = hospitalService.findHospitalByHoscode(hoscode);
        if (Objects.isNull(dbHospital)) {
            throw new HospitalException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = dbHospital.getBookingRule();
        // ?????????????????????,?????????7???????????????
        IPage<Date> iPage = this.getListDate(page, limit, bookingRule);
        List<Date> dateList = iPage.getRecords();
        // ??????????????????????????????????????????
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode)
                .and("workDate").in(dateList);
        // ??????
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                //????????????
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregateResults =
                mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleRuleVoList = aggregateResults.getMappedResults();
        // ???????????????????????????
        // ????????????,???????????????ScheduleRuleVo???????????????????????????BookingRuleVo
        Map<Date, BookingScheduleRuleVo> scheduleRuleVoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(scheduleRuleVoList)) {
            scheduleRuleVoMap = scheduleRuleVoList.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, bookingScheduleRuleVo -> bookingScheduleRuleVo));
        }
        // ???????????????????????????
        List<BookingScheduleRuleVo> bookingScheduleRuleVos = new ArrayList<>();
        for (int i = 0, len = dateList.size(); i < len; i++) {
            Date date = dateList.get(i);
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleRuleVoMap.get(date);
            if (Objects.isNull(bookingScheduleRuleVo)) {
                // ??????????????????????????????
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                // ??????????????????
                bookingScheduleRuleVo.setDocCount(0);
                // ?????????????????????  -1????????????
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            // ?????????????????????????????????
            String dayOfWeek = JodaTimeUtils.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
            // ?????????????????????????????????????????????  ??????0??????.1????????????.-1???????????????
            if (i == len - 1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            // ????????????????????????????????????, ???????????????
            if (i == 0 && page == 1) {
                DateTime stopTime = JodaTimeUtils.getDateTime(new Date(), bookingRule.getStopTime());
                if (stopTime.isBeforeNow()) {
                    // ????????????
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVos.add(bookingScheduleRuleVo);
        }
        // ???????????????????????????
        result.put("bookingScheduleList", bookingScheduleRuleVos);
        result.put("total", iPage.getTotal());
        // ??????????????????
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", dbHospital.getHosname());
        Department department = departmentRepository.findDepartmentByHoscodeAndDepcode(hoscode, depcode);
        // ???????????????
        baseMap.put("bigname", department.getBigname());
        // ????????????
        baseMap.put("depname", department.getDepname());
        // ???
        baseMap.put("workDateString", new DateTime().toString("yyyy???MM???"));
        //????????????
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        //????????????
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;

    }


    /**
     * ?????????????????????????????????
     */
    private IPage<Date> getListDate(int page, int limit, BookingRule bookingRule) {
        //??????????????????
        DateTime releaseTime = JodaTimeUtils.getDateTime(new Date(), bookingRule.getReleaseTime());
        //????????????
        int cycle = bookingRule.getCycle();
        //??????????????????????????????????????????????????????????????????????????????????????????1
        if (releaseTime.isBeforeNow()) {
            cycle += 1;
        }
        //???????????????????????????????????????????????????????????????
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
        //????????????????????????
            DateTime curDateTime = new DateTime().plusDays(i);
            String dateString = curDateTime.toString("yyyy-MM-dd");
            dateList.add(new DateTime(dateString).toDate());
        }
        //?????????????????????????????????????????????????????????????????????7????????????????????????????????????
        List<Date> pageDateList = new ArrayList<>();
        int start = (page - 1) * limit;
        int end = (page - 1) * limit + limit;
        if (end > dateList.size()) {
            end = dateList.size();
        }
        for (int i = start; i < end; i++) {
            pageDateList.add(dateList.get(i));
        }
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page(page, 7, dateList.size());
        iPage.setRecords(pageDateList);
        return iPage;
    }

    public Schedule getById(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        packageSchedule(schedule);
        return schedule;
    }

    //????????????id????????????????????????
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        //????????????
        Schedule schedule = getById(scheduleId);
        if(null == schedule) {
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }

        //????????????????????????
        Hospital hospital = hospitalService.findHospitalByHoscode(schedule.getHoscode());
        if(null == hospital) {
            throw new HospitalException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        if(null == bookingRule) {
            throw new HospitalException(ResultCodeEnum.PARAM_ERROR);
        }

        scheduleOrderVo.setHoscode(schedule.getHoscode());
        scheduleOrderVo.setHosname(hospital.getHosname());
        scheduleOrderVo.setDepcode(schedule.getDepcode());
        scheduleOrderVo.setDepname(departmentService.getDepartmentByHoscodeAndDepcode(schedule.getHoscode(), schedule.getDepcode()).getDepname());
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());

        //?????????????????????????????????????????????-1????????????0???
        int quitDay = bookingRule.getQuitDay();
        DateTime quitTime = JodaTimeUtils.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(), bookingRule.getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());

        //??????????????????
        DateTime startTime = JodaTimeUtils.getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());

        //??????????????????
        DateTime endTime = JodaTimeUtils.getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        //????????????????????????
        DateTime stopTime = JodaTimeUtils.getDateTime(new Date(), bookingRule.getStopTime());
        scheduleOrderVo.setStopTime(stopTime.toDate());
        return scheduleOrderVo;
    }

    /**
     * ????????????
     * @param schedule
     */
    public void update(Schedule schedule) {
        schedule.setUpdateTime(new Date());
        //????????????????????????
        scheduleRepository.save(schedule);
    }

}