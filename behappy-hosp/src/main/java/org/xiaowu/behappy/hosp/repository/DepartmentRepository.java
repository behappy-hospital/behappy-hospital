package org.xiaowu.behappy.hosp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xiaowu.behappy.hosp.entity.Department;

@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {

    Department findDepartmentByHoscodeAndDepcode(@Param("hoscode")String hoscode,
                                                 @Param("depcode")String depcode);

    int deleteDepartmentByHoscodeAndDepcode(@Param("hoscode")String hoscode,
                                            @Param("depcode")String depcode);
}
