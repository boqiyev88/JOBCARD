package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.City;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, String> {

    @Query("select c from City c ")
    Optional<List<City>> getByIsDeleted();

    @Query("select c from City c where c.ID= :id")
    Optional<City> findById(String id);
}
