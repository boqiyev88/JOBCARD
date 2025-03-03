package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Work;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {

}
