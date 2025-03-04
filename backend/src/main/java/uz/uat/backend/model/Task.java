package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TASK")
@Builder
public class Task extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false,name = "NUMBER")
    private String Number;

    @Column(nullable = false, name = "DESCRIPTION")
    private String description;

}
