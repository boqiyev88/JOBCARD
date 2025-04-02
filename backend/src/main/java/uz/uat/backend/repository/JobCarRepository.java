package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import uz.uat.backend.dto.StatusCountDto;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.enums.Status;

import java.util.List;
import java.util.Optional;

public interface JobCarRepository extends JpaRepository<JobCard, String> {

    @Transactional
    @Query("select sj from JobCard sj where sj.id= :jobId and sj.isDeleted =0")
    JobCard findByJobCardId(@Param("jobId") String jobId);

    @Query("select new uz.uat.backend.dto.StatusCountDto(cast(j.status as string ),count(j)) from JobCard j where j.isDeleted=0 group by j.status")
    List<StatusCountDto> getByStatusCount();

    @Transactional
    @Query("select sj from JobCard sj where sj.status = :status and sj.isDeleted =0 ORDER BY sj.createdDate desc , sj.updTime desc")
    Page<JobCard> findBySTATUS(@Param("status") Status status, Pageable pageable);

    @Query("select sj from JobCard sj where  sj.isDeleted =0 ORDER BY sj.createdDate desc , sj.updTime desc ")
    Page<JobCard> getAll(Pageable pageable);

    @Transactional
    @Query("select sj from JobCard sj where sj.work_order= :workOrderNumber and  sj.isDeleted =0")
    Optional<JobCard> findByWorkOrderNumber(@Param("workOrderNumber") String workOrderNumber);

    @Transactional
    @Query("""
            SELECT j FROM JobCard j  WHERE j.isDeleted = 0 AND((:name IS NULL OR j.status = :name) OR (:search IS NULL OR LOWER(j.reg) LIKE LOWER(CONCAT('%', :search, '%'))))""")
    Page<JobCard> findByStatusAndSearch(@Param("name") Status name, @Param("search") String search,Pageable pageable);

    @Transactional
    @Query(" SELECT j FROM JobCard j  WHERE j.isDeleted = 0 AND (:search IS NULL OR LOWER(j.reg) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<JobCard> findBySearch(String search, Pageable pageable);
}
