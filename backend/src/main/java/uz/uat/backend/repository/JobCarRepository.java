package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.enums.Status;

import java.util.List;
import java.util.Optional;

public interface JobCarRepository extends JpaRepository<JobCard, String> {

    @Transactional
    @Query("select sj.mainPlan from JobCard sj where sj.id= :jobId and sj.isDeleted =0")
    JobCard findByJobCardId(@Param("jobId") String jobId);


    @Transactional
    @Query("select sj from JobCard sj where sj.STATUS = :status and sj.isDeleted =0")
    Optional<List<JobCard>> findBySTATUS(@Param("status") String status);

    @Query("select sj from JobCard sj where  sj.isDeleted =0")
    Optional<List<JobCard>> getAll();

    @Transactional
    @Modifying
    @Query("update JobCard s set s.STATUS = ?1 where s.id = ?2")
    JobCard updateSTATUSById(String STATUS, String id);

}
