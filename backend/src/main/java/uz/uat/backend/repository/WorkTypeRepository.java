package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.WorkType;

import java.util.List;

@Repository
public interface WorkTypeRepository extends JpaRepository<WorkType, String> {

    @Query("select w from WorkType w where w.isDeleted=0")
    List<WorkType> getWorkTypes();

}
