package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Work;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {

    @Query("select w from Work w where w.isDeleted =0 ORDER BY w.updTime desc ,w.createdDate desc")
    Page<Work> getAll(Pageable pageable);

    @Query("select w from Work w where w.isDeleted =0 and w.jobcard_id.id =:jobid ORDER BY w.updTime desc ,w.createdDate desc")
    List<Work> findByJobcard_id(@Param("jobid")String jobid);

    @Query("select w from Work w where w.isDeleted=0 and w.id =:workid")
    Optional<Work> findWorkById(@Param("workid")String workid);

    @Query("SELECT s FROM Work s WHERE s.isDeleted=0 and s.id IN :ids")
    List<Work> findAllByIds(Set<String> workids);
}
