package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import uz.uat.backend.model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SERVICES")
@Builder
public class Services extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String id;

    @JoinColumn(nullable = false, name = "SERVICETYPE")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiceType serviceType;

    @JoinColumn(nullable = false, name = "SERVICENAME")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiceName serviceName;

    @ManyToMany
    @JoinTable(
            name = "SERVICE_TASK",
            schema = "uat",
            joinColumns = @JoinColumn(name = "SERVICE_ID"),
            inverseJoinColumns = @JoinColumn(name = "TASK_ID")
    )
    private List<Task> tasks;
    @Column(nullable = false, name = "REVISONNUMBER")
    private String revisionNumber;

    @Column(nullable = false, name = "REVISONTIME")
    private LocalDateTime revisionTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;


}
