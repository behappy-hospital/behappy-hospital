package org.xiaowu.behappy.hosp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.xiaowu.behappy.hosp.entity.Hospital;

/**
 * @author xiaowu
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    Hospital findHospitalByHoscode(String hoscode);
}
