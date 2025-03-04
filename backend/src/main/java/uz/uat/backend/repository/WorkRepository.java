package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Work;
import uz.uat.backend.model.WorkType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {

//    @Query("SELECT w.id,w.workType,w.taskList FROM Work w")
//    List<Work> getWork();
//
//    @Query("SELECT w.id,w.workType,w.taskList FROM Work w " +
//            "JOIN w.taskList t WHERE t.revisionTime BETWEEN :startDate AND :endDate")
//    List<Work> searchWorkByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
