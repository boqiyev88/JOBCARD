package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Services;

@Repository
public interface ServicesRepository extends JpaRepository<Services, String> {
}
