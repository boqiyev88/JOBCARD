package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.history_models.Technician_History;

@Repository
public interface Technician_HistoryRepository extends JpaRepository<Technician_History, String> {
}
