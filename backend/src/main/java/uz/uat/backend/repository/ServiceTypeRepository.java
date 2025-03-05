package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.ServiceType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, String> {


    @Query("select sn from ServiceType sn where sn.NAME= :name and sn.isDeleted=0")
    Optional<ServiceType> findByName(@Param("name") String name);

    @Query("select sn from ServiceType sn where sn.isDeleted=0")
    Optional<List<ServiceType>> getServiceType();
}
