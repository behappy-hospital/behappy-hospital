//package org.xiaowu.behappy.hosp.canal;
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.xiaowu.behappy.canal.client.handler.EntryHandler;
//import org.xiaowu.behappy.hosp.entity.HospitalSet;
//import org.xiaowu.behappy.hosp.service.HospitalSetService;
//
///**
// * 监听数据库binlog，处理缓存数据
// * @author 小五
// */
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class HospitalSetHandler implements EntryHandler<HospitalSet> {
//
//    private final HospitalSetService hospitalSetService;
//
//    @Override
//    public void insert(HospitalSet hospitalSet) {
//        hospitalSetService.saveHospitalSet(hospitalSet);
//    }
//
//    @Override
//    public void update(HospitalSet before, HospitalSet after) {
//        hospitalSetService.delHospitalSet(before.getHoscode());
//    }
//
//    @Override
//    public void delete(HospitalSet hospitalSet) {
//        hospitalSetService.delHospitalSet(hospitalSet.getHoscode());
//    }
//}
