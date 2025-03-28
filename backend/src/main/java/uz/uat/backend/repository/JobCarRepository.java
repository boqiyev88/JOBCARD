package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.enums.Status;

import java.util.Optional;

public interface JobCarRepository extends JpaRepository<JobCard, String> {

    @Transactional
    @Query("select sj from JobCard sj where sj.id= :jobId and sj.isDeleted =0")
    JobCard findByJobCardId(@Param("jobId") String jobId);


    @Transactional
    @Query("select sj from JobCard sj where sj.status = :status and sj.isDeleted =0 ORDER BY sj.createdDate desc , sj.updTime desc")
    Page<JobCard> findBySTATUS(@Param("status") Status status, Pageable pageable);

    @Query("select sj from JobCard sj where  sj.isDeleted =0 ORDER BY sj.createdDate desc , sj.updTime desc ")
    Page<JobCard> getAll(Pageable pageable);

    @Transactional
    @Query("select sj from JobCard sj where sj.work_order= :workOrderNumber and  sj.isDeleted =0")
    Optional<JobCard> findByWorkOrderNumber(@Param("workOrderNumber") String workOrderNumber);
}
