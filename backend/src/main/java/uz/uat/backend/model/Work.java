package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import uz.uat.backend.model.enums.Status;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "WORK", schema = "uat")
public class Work extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false)
    private String threshold;

    @Column(nullable = false)
    private String repeat_int;

    @Column(nullable = false)
    private String zone;

    @Column(nullable = false)
    private String mpr;

    @Column(nullable = false)
    private String access;

    @Column(nullable = false)
    private String airplane_app;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String access_note;

    @Column(nullable = false)
    private String task_description;

    @Column(nullable = false)
    private int dit;

    @Column(name = "avionic")
    @Builder.Default
    private int avionic = 0;

    @Column(name = "mechanic")
    @Builder.Default
    private int mechanic = 0;

    @Column(name = "cab_mechanic")
    @Builder.Default
    private int cab_mechanic = 0;

    @Column(name = "sheet_metal")
    @Builder.Default
    private int sheet_metal = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.NEW;

    @Column(name = "ndt")
    @Builder.Default
    private int ndt = 0;

    @JoinColumn(name = "jobcard_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private JobCard jobcard_id;

    @JoinColumn(name = "service_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Services service_id;


}
