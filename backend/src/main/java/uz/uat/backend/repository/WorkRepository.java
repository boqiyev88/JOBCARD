package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Work;
import uz.uat.backend.model.WorkType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {

    @Query("select w from Work w where w.isDeleted =0 ORDER BY w.updTime desc ,w.createdDate desc")
    Page<Work> getAll(Pageable pageable);


}
