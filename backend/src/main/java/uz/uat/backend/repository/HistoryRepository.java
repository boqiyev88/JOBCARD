package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.history_models.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, String> {
}