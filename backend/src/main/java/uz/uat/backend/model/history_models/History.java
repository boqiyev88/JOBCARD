package uz.uat.backend.model.history_models;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import uz.uat.backend.model.enums.OperationStatus;
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

    @Column(name = "TABLENAME")
    @Enumerated(EnumType.STRING)
    private TableName tablename;

    @Column(name = "TABLEID")
    private String tableID;

    @Column(name = "OS")
    @Enumerated(EnumType.STRING)
    private OperationStatus OS;

    @Column(name = "ROWNAME")
    private String rowName;

    @Column(name = "OLDVALUE")
    private String oldValue;

    @Column(name = "NEWVALUE")
    private String newValue;

    @Column(name = "UPDATEDBY")
    private String updatedBy;

    @Column(name = "UPDTIME")
    @Builder.Default
    private Instant updTime = Instant.now();


}
