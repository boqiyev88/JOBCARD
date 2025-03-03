package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.TaskType;



@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, String> {

}
