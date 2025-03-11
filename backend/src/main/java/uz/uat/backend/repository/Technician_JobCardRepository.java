package uz.uat.backend.repository;

import feign.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.uat.backend.model.Technician_JobCard;
import uz.uat.backend.model.enums.Status;

import java.util.Optional;


@Repository
public interface Technician_JobCardRepository extends JpaRepository<Technician_JobCard, String> {

    @Query("select tj from Technician_JobCard tj where tj.WorkOrderNumber= :WON and tj.isDeleted=0")
    Optional<Technician_JobCard> findByWorkOrderNumber(@Param("WON") String WON);

    @Transactional
    @Modifying
    @Query("update Technician_JobCard t set t.STATUS = ?1 where t.WorkOrderNumber = ?2")
    Technician_JobCard updateSTATUSByWorkOrderNumber(Status STATUS, String WorkOrderNumber);
}
