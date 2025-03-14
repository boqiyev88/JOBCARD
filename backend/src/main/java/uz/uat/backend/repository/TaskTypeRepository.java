package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.ServiceName;



@Repository
public interface TaskTypeRepository extends JpaRepository<ServiceName, String> {

}
