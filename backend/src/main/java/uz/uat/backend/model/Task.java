package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TASK")
public class Task extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false,name = "DESCRIPTION")
    private String description;

    @JoinColumn(nullable = false,name = "TASKTYPE")
    @ManyToOne(fetch = FetchType.EAGER)
    private TaskType taskType;

    @Column(nullable = false,name = "REVISONNUMBER")
    private String revisionNumber;

    @Column(nullable = false,name = "REVISONTIME")
    private LocalDateTime revisionTime;

}
