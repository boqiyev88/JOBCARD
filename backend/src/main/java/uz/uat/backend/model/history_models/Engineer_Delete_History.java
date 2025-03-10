package uz.uat.backend.model.history_models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Engineer_Delete_History", schema = "uat")
@Builder
public class Engineer_Delete_History {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String ID;


    @Column(name = "TABLEID")
    private String tableID;

    @Column(name = "OLDVALUE")
    private String oldValue;

    @Column(name = "NEWVALUE")
    private String newValue;

    @Column(name = "DELUSER")
    private String deletedBy;

    @Column(name = "DELTIME")
    private Instant delTime;

}