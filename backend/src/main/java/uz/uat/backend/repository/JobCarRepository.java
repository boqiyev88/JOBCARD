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
    @Query("select sj from JobCard sj where sj.id= :jobId and sj.isDeleted =0")
    JobCard findByJobCardId(@Param("jobId") String jobId);


    @Transactional
    @Query("select sj from JobCard sj where sj.status = :status and sj.isDeleted =0")
    List<JobCard> findBySTATUS(@Param("status") Status status);

    @Query("select sj from JobCard sj where  sj.isDeleted =0")
    List<JobCard> getAll();

    @Transactional
    @Modifying
    @Query("update JobCard s set s.status = ?1 where s.id = ?2")
    JobCard updateSTATUSById(Status STATUS, String id);

    @Transactional
    @Query("select sj from JobCard sj where sj.workOrder= :workOrderNumber and  sj.isDeleted =0")
    Optional<JobCard> findByWorkOrderNumber(@Param("workOrderNumber") String workOrderNumber);
}
