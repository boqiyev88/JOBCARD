package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Task;



@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

}
