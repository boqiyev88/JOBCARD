package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "WORK")
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
    private String DESCRIPTION;

    @JoinColumn(nullable = false)
    @OneToMany(fetch = FetchType.EAGER)
    private List<Task> taskList;

    @JoinColumn(nullable = false,name = "WORKTYPE_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private WorkType workType;

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


}
