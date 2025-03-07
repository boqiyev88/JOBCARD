package uz.uat.backend.repository;

import feign.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Technician_JobCard;

import java.util.Optional;


@Repository
public interface Technician_JobCardRepository extends JpaRepository<Technician_JobCard, String> {

    @Query("select tj from Technician_JobCard tj where tj.WorkOrderNumber= :WON and tj.isDeleted=0")
    Optional<Technician_JobCard> findByWorkOrderNumber(@Param("WON") String WON);

}
