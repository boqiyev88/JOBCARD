package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.ServiceName;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceNameRepository extends JpaRepository<ServiceName, String> {

    @Query("select sn from ServiceName sn where sn.id= :id and sn.isDeleted=0")
    Optional<ServiceName> findByName(@Param("id") String id);

    @Query("select sn from ServiceName sn where sn.isDeleted=0")
    Optional<List<ServiceName>> getServiceName();
}
