package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity(name = "message")
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

    @JoinColumn(name = "job_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private JobCard job_id;

    @JoinColumn(name = "from_user")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private User fromUser;

    @JoinColumn(name = "to_user")
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    private User toUser;

    @Column(nullable = false)
    private LocalDateTime created_date = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false, columnDefinition = "SMALLINT DEFAULT 0")
    private int is_deleted = 0;


}
