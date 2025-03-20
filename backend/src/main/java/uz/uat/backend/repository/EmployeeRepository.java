package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Employeer;

@Repository
public interface EmployeeRepository extends JpaRepository<Employeer, String> {

}
