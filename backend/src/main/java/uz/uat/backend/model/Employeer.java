package uz.uat.backend.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employeer", schema = "uat")
@Builder
public class Employeer {


    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(name = "avionic")
    private int avionic=0;

    @Column(name = "mechanic")
    private int mechanic=0;

    @Column(name = "cab_mechanic")
    private int cab_mechanic=0;

    @Column(name = "sheet_metal")
    private int sheet_metal=0;

    @Column(name = "ndt")
    private int ndt=0;
}
