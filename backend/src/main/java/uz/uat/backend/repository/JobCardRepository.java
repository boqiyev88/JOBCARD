package uz.uat.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.JobCard;


@Repository
public interface JobCardRepository extends JpaRepository<JobCard, String> {

}
