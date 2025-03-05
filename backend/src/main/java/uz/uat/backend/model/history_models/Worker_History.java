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
@Table(name = "WORKER_HISTORY", schema = "uat")
@Builder
public class Worker_History {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String ID;

    @Column(name = "TABLEID")
    private String tableID;

    @Column(name = "ROWNAME")
    private String rowName;

    @Column(name = "OLDVALUE")
    private String oldValue;

    @Column(name = "NEWVALUE")
    private String newValue;

    @Column(name = "UPDATEDBY")
    private String updatedBy;

    @Column(name = "UPDTIME")
    private Instant updTime;


}