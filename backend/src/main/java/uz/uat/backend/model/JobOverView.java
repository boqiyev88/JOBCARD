package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "JOB")
@Builder
public class JobOverView extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false)
    private String WorkOrderNumber;

    @Column(nullable = false)
    private String REG;

    @Column(nullable = false)
    private String SerialNumber1;

    @Column(nullable = false)
    private String ENGINE_1;

    @Column(nullable = false)
    private String SerialNumber2;

    @Column(nullable = false)
    private String ENGINE_2;

    @Column(nullable = false)
    private String SerialNumber3;

    @Column(nullable = false)
    private String APU;

    @Column(nullable = false)
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
    private LocalDateTime DATE;

    @JoinColumn(name = "MAIN_PLAN")
    @OneToOne(fetch = FetchType.EAGER)
    private PdfFile mainPlan;

}
