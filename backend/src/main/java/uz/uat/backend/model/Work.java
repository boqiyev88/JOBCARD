package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

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
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false)
    private String threshold;

    @Column(nullable = false)
    private String repeat_int;

    @Column(nullable = false)
    private String zone;

    @Column(nullable = false)
    private String mrf;

    @Column(nullable = false)
    private String access;

    @Column(nullable = false)
    private String airplane_app;

    @Column(nullable = false)
    private String access_note;

    @Column(nullable = false)
    private String task_description;

    @Column(nullable = false)
    private boolean dit;

    @JoinColumn(name = "jobCard_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private JobCard jobCard_id;

    @JoinColumn(name = "service_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Services service_id;

    @JoinColumn(name = "workers_names")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Employeer workers_names;


}
