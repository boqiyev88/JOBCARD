package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "services", schema = "uat")
@Builder
public class Services extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @JoinColumn(nullable = false, name = "servicetype")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiceType serviceType;

    @JoinColumn(nullable = false, name = "servicename")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiceName serviceName;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "service_task",
            schema = "uat",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks;

    @Column(nullable = false, name = "revisionnumber")
    private String revisionNumber;

    @Column(nullable = false, name = "revisiontime")
    private LocalDate   revisionTime;


}
