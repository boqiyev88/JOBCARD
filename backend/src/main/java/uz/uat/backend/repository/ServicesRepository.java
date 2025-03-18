package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services, String> {

    @Query("SELECT s FROM Services s where s.revisionTime BETWEEN :startDate AND :endDate  and s.isDeleted=0")
    List<Services> searchServicesByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select s from Services s where s.id = :id and s.isDeleted=0")
    Optional<Services> findById(@Param("id") String id);

    @EntityGraph(attributePaths = "tasks")
    @Query("select s from Services s where s.isDeleted=0")
    List<Services> getAll();

    @Query("select s from Services s where s.id = :id and s.isDeleted=0")
    Services getById(@Param("id") String id);


}
