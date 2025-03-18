package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Task;

import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

}
