package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Specialist_JobCard;

@Repository
public interface Specialist_JobCardRepository extends JpaRepository<Specialist_JobCard, String> {

    @Query("select sj.mainPlan from Specialist_JobCard sj where sj.id= :jobId and sj.isDeleted =0")
    Specialist_JobCard findByJobCardId(@Param("jobId") String jobId);
}
