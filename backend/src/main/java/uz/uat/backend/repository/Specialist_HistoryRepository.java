package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.history_models.Specialist_History;

@Repository
public interface Specialist_HistoryRepository extends JpaRepository<Specialist_History, String> {
}