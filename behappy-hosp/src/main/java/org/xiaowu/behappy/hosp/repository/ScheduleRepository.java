package org.xiaowu.behappy.hosp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xiaowu.behappy.api.hosp.model.Schedule;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {

    Schedule getScheduleByHoscodeAndHosScheduleId(@Param("hoscode") String hoscode,
                                                  @Param("hosScheduleId") String hosScheduleId);

    int deleteScheduleByHoscodeAndHosScheduleId(@Param("hoscode") String hoscode,
                                                  @Param("hosScheduleId") String hosScheduleId);

}
