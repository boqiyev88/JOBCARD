package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import uz.uat.backend.model.enums.Status;

import java.time.Instant;
import java.time.LocalDateTime;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "JOB", schema = "uat")
@Builder
public class JobCard extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false, name = "work_order")
    private String workOrder;

    @Column(nullable = false)
    private String reg;

    @Column(nullable = false, name = "serial_number_1")
    private String serialNumber1;

    @Column(nullable = false)
    private String engine_1;

    @Column(nullable = false, name = "serial_number_2")
    private String serialNumber2;

    @Column(nullable = false)
    private String engine_2;

    @Column(nullable = false, name = "serial_number_3")
    private String serialNumber3;

    @Column(nullable = false)
    private String apu;

    @Column(nullable = false, name = "serial_number_4")
    private String serialNumber4;

    @Column(nullable = false,name = "beforelight")
    private String beforelight;

    @Column(nullable = false)
    private String fh;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private City leg;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private City to;

    @Column(nullable = false, name = "date")
    private Instant date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @JoinColumn(name = "MAIN_PLAN")
    @OneToOne(fetch = FetchType.EAGER)
    private PdfFile mainPlan;

}
