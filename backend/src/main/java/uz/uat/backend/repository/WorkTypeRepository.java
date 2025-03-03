package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.WorkType;

@Repository
public interface WorkTypeRepository extends JpaRepository<WorkType, String> {

}
