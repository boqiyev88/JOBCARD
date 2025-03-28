package uz.uat.backend.repository;

import feign.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Services> searchServicesByDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable page);

    @NotNull
    @Query("select s from Services s where s.id = :id and s.isDeleted=0")
    Optional<Services> findById(@Param("id") String id);

    @Query("select s from Services s where s.isDeleted=0 ORDER BY s.updTime desc ,s.createdDate desc ")
    List<Services> getAll();

    @Query("select s from Services s where s.isDeleted=0 ORDER BY s.updTime desc ,s.createdDate desc ")
    Page<Services> getByPage(Pageable page);


    @Query("SELECT s FROM Services s WHERE s.isDeleted=0 and LOWER(s.serviceName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.serviceType) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Services> searchByNameOrType(@Param("search") String search, Pageable page);

    @Query("SELECT s FROM Services s WHERE s.isDeleted=0 and s.revisionTime <= :toDate ORDER BY s.updTime desc ,s.createdDate desc")
    Page<Services> getByToDate(@Param("toDate") LocalDate toDate, Pageable pageable);
}
