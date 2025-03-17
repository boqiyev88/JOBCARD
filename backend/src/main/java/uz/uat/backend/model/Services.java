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
@Table(name = "SERVICES",schema = "uat")
@Builder
public class Services extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String id;

    @JoinColumn(nullable = false, name = "SERVICETYPE")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiceType SERVICETYPE;

    @JoinColumn(nullable = false, name = "SERVICENAME")
    @ManyToOne(fetch = FetchType.EAGER)
    private ServiceName SERVICENAME;

    @ManyToMany
    @JoinTable(
            name = "SERVICE_TASK",
            schema = "uat",
            joinColumns = @JoinColumn(name = "SERVICE_ID"),
            inverseJoinColumns = @JoinColumn(name = "TASK_ID")
    )
    private List<Task> tasks;
    @Column(nullable = false, name = "REVISONNUMBER")
    private String REVISONNUMBER;

    @Column(nullable = false, name = "REVISONTIME")
    private LocalDate REVISONTIME;


}
