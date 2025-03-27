package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RTABLES", schema = "uat")
@Builder
public class RTables {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @JoinColumn(name = "service_table_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Services service_table_id;

    @JoinColumn(name = "job_table_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private JobCard job_table_id;

    @JoinColumn(name = "work_table_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Work work_table_id;
}
