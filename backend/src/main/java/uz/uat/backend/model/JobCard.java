package uz.uat.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import feign.codec.Decoder;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import uz.uat.backend.model.enums.Status;

import java.time.Instant;
import java.time.LocalDate;
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
    private String work_order;

    @Column(nullable = false)
    private String reg;

    @Column(nullable = false, name = "serial_number_1")
    private String serial_number1;

    @Column(nullable = false)
    private String engine_1;

    @Column(nullable = false, name = "serial_number_2")
    private String serial_number2;

    @Column(nullable = false)
    private String engine_2;

    @Column(nullable = false, name = "serial_number_3")
    private String serial_number3;

    @Column(nullable = false)
    private String apu;

    @Column(nullable = false, name = "serial_number_4")
    private String serial_number4;

    @Column(nullable = false, name = "before_flight")
    private String before_flight;

    @Column(nullable = false)
    private String fh;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private City leg;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private City to;

    @Column(nullable = false, name = "date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @JoinColumn(name = "MAIN_PLAN")
    @OneToOne
    private PdfFile mainPlan;

}
