package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import uz.uat.backend.dto.Worker;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "WORK", schema = "uat")
public class Work extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false)
    private String THRESHOLD;

    @Column(nullable = false)
    private String REPEAT_INT;

    @Column(nullable = false)
    private String ZONE;

    @Column(nullable = false)
    private String MRF;

    @Column(nullable = false)
    private String ACCESS;

    @Column(nullable = false)
    private String AIRPLANE_APP;

    @Column(nullable = false)
    private String ACCESS_NOTE;

    @Column(nullable = false)
    private String TASK_DESCRIPTION;

    @Column(nullable = false)
    private boolean DIT;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Technician_JobCard T_JOBCARD_ID;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Services SERVICE_ID;

    @JoinColumn
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Employeer workers_names;


}
