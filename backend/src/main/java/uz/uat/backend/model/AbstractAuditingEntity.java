package uz.uat.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AbstractAuditingEntity {

    @CreatedBy
    @Column(name = "INSUSER", length = 50, updatable = false)
    private String insUser;

    @LastModifiedBy
    @Column(name = "UPDUSER")
    private String updUser;

    @CreatedDate
    @Column(name = "INSTIME", updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "UPDTIME")
    private Instant updTime;

    @Column(name = "DELTIME")
    private Instant delTime;

    @Column(name = "DELUSER")
    private String delUser;

    @Column(name = "ISDELETED", columnDefinition = " SMALLINT DEFAULT 0")
    private int isDeleted = 0;


}