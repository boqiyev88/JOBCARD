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
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false, name = "WORK_ORDER_NUMBER")
    private String WorkOrderNumber;

    @Column(nullable = false)
    private String REG;

    @Column(nullable = false, name = "SERIAL_NUMBER_1")
    private String SerialNumber1;

    @Column(nullable = false)
    private String ENGINE_1;

    @Column(nullable = false, name = "SERIAL_NUMBER_2")
    private String SerialNumber2;

    @Column(nullable = false)
    private String ENGINE_2;

    @Column(nullable = false, name = "SERIAL_NUMBER_3")
    private String SerialNumber3;

    @Column(nullable = false)
    private String APU;

    @Column(nullable = false, name = "SERIAL_NUMBER_4")
    private String SerialNumber4;

    @Column(nullable = false)
    private String BEFORELIGHT;

    @Column(nullable = false)
    private String FH;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private City LEG;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private City TO;

    @Column(nullable = false, name = "DATE")
    private Instant DATE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status STATUS = Status.NEW;

    @JoinColumn(name = "MAIN_PLAN")
    @OneToOne(fetch = FetchType.EAGER)
    private PdfFile mainPlan;

}
