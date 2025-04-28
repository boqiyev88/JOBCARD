package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Message;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    @Query("select m from Message m where m.isDeleted=0 and m.jobId= :jobId")
    Optional<Message> findByJobId(@Param("jobId") String jobId);
}
