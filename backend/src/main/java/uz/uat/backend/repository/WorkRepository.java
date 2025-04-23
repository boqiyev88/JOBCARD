package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.dto.StatusCountDto;
import uz.uat.backend.model.Work;
import uz.uat.backend.model.enums.Status;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {

    @Query("select w from Work w where w.isDeleted =0 ORDER BY w.updTime desc ,w.createdDate desc")
    Page<Work> getAll(Pageable pageable);

    @Query("select w from Work w where w.isDeleted =0 and w.jobcard_id.id =:jobid ORDER BY w.updTime desc ,w.createdDate desc")
    List<Work> findByJobcard_id(@Param("jobid") String jobid);

    @Query("select w from Work w where w.isDeleted=0 and w.id =:workid")
    Optional<Work> findWorkById(@Param("workid") String workid);

    @Query("select new uz.uat.backend.dto.StatusCountDto(cast(j.status as string ),count(j)) from Work j where j.isDeleted=0 group by j.status")
    List<StatusCountDto> getByStatusCount();

    @Query("select w from Work w where w.isDeleted=0")
    Optional<Work> findWorkByStatus();

    @Query("SELECT w FROM Work w WHERE w.isDeleted = 0 AND w.status =: status AND  w.jobcard_id.id IN :jobIds")
    List<Work> findNewWorksByJobIds(@Param("status") Status status, @Param("jobIds") Set<String> jobIds);


}
