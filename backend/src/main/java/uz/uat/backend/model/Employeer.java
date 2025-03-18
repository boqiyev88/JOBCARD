package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employeer", schema = "uat")
public class Employeer {


    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column
    private boolean avionic;
    @Column
    private boolean mechanic;
    @Column
    private boolean cab_mechanic;
    @Column
    private boolean sheet_metal;
    @Column
    private boolean NDT;
}
