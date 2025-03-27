package uz.uat.backend.model.history_models;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import uz.uat.backend.model.enums.TableName;

import java.time.Instant;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HISOTRY", schema = "uat")
@Builder
public class History {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String ID;

    @Column(name = "TABLENAME", nullable = false)
    private TableName tablename;

    @Column(name = "TABLEID", nullable = false)
    private String tableID;

    @Column(name = "DESCRITION", nullable = false)
    private String description;

    @Column(name = "ROWNAME", nullable = false)
    private String rowName;

    @Column(name = "OLDVALUE", nullable = false)
    private String oldValue;

    @Column(name = "NEWVALUE", nullable = false)
    private String newValue;

    @Column(name = "UPDATEDBY")
    private String updatedBy;

    @Column(name = "UPDTIME", nullable = false)
    private Instant updTime;


}
