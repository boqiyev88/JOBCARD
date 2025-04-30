package uz.uat.backend.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.uat.backend.model.Message;
import uz.uat.backend.model.Work;
import uz.uat.backend.model.enums.Status;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    @Query("select m from message m where m.is_deleted=0 and m.job_id.id= :jobId")
    Optional<Message> findByJobId(@Param("jobId") String jobId);

    @Query("SELECT w FROM message w WHERE w.is_deleted = 0  AND w.job_id.id IN :jobIds")
    List<Message> findMessageByJobIds( @Param("jobIds") Set<String> jobIds);
}
