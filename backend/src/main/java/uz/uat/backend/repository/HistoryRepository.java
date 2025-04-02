package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Services;
import uz.uat.backend.model.history_models.History;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, String> {

    @Query("select h from History h order by h.updTime desc , h.updatedBy desc ")
    Page<History> getAll(PageRequest of);

    @Query("SELECT s FROM History s where LOWER(s.tablename) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Page<History> searchByTablename(@Param("search") String search, Pageable page);
}