package org.xiaowu.behappy.hosp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xiaowu.behappy.hosp.entity.Schedule;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {

    Schedule getScheduleByHoscodeAndHosScheduleId(@Param("hoscode") String hoscode,
                                                  @Param("hosScheduleId") String hosScheduleId);

    int deleteScheduleByHoscodeAndHosScheduleId(@Param("hoscode") String hoscode,
                                                  @Param("hosScheduleId") String hosScheduleId);

    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(@Param("hoscode") String hoscode,
                                                    @Param("depcode") String depcode,
                                                    @Param("workDate") Date workDate);

}
