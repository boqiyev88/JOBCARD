package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message", schema = "uat")
@Builder
public class Message {
    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(length = 50)
    private String jobId;

    @JoinColumn(name = "from_user")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private User fromUser;

    @JoinColumn(name = "to_user")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private User toUser;

    @Column(nullable = false)
    private LocalDateTime created_date = LocalDateTime.now();

    @Column(nullable = false, columnDefinition = "SMALLINT DEFAULT 0")
    private int isDeleted = 0;


}
