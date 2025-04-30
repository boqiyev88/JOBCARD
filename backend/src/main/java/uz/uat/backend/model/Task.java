package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task", schema = "uat")
@Builder
public class Task {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false, name = "number")
    private String number;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(name = "pref")
    @Builder.Default
    private int pref = 0;

    @Column(name = "insp")
    @Builder.Default
    private int insp = 0;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user_id;

}
