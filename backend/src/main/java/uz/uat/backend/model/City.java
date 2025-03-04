package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import uz.uat.backend.model.enums.Status;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CITY",schema = "uat")
public class City extends AbstractAuditingEntity{

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String ID;

    @Column(nullable = false)
    private String NAME;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status STATUS;

}
