package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.uat.backend.model.Specialist_JobCard;
import uz.uat.backend.model.enums.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface Specialist_JobCardRepository extends JpaRepository<Specialist_JobCard, String> {

    @Query("select sj.mainPlan from Specialist_JobCard sj where sj.id= :jobId and sj.isDeleted =0")
    Specialist_JobCard findByJobCardId(@Param("jobId") String jobId);


    @Query("select sj from Specialist_JobCard sj where sj.STATUS = :status and sj.isDeleted =0")
    Optional<List<Specialist_JobCard>> findBySTATUS(@Param("status") String status);

     @Query("select sj from Specialist_JobCard sj where  sj.isDeleted =0")
    Optional<List<Specialist_JobCard>> getAll();

    @Transactional
    @Modifying
    @Query("update Specialist_JobCard s set s.STATUS = ?1 where s.id = ?2")
    Specialist_JobCard updateSTATUSById(Status STATUS, String id);

}
