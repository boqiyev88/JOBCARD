package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER")
public class User extends AbstractAuditingEntity implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "ID", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false, name = "USERNAME", columnDefinition = "VARCHAR(20)")
    private String username;

    @Column(nullable = false, name = "PASSWORD", columnDefinition = "VARCHAR(100)")
    private String password;

    @Column(nullable = false, name = "PASSWORDOPEN", columnDefinition = "VARCHAR(20)")
    private String passwordOpen;

    @Column(name = "PINFL", columnDefinition = "VARCHAR(14)")
    private String pinfl;

    @Column(name = "FIRSTNAME", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String firstName;

    @Column(name = "LASTNAME", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String lastName;

    @Column(name = "FATHERNAME", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String fatherName;

    @Column(name = "FULLNAME", columnDefinition = "VARCHAR(1800) CCSID 1208")
    private String fullName;

    @Column(name = "PASSPORT_NO", columnDefinition = "VARCHAR(10)")
    private String passportNo = "";

    @Column(name = "PASSPORT_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date passportDate;

    @Column(name = "PASSPORT_ISSUED", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String passportIssued = "";

    @Column(name = "PASSPORT_BIRTHDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date passportBirthDate;

    @Column(name = "ADDRESS", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String address = "";

    @Column(name = "PHONE", columnDefinition = "VARCHAR(20)")
    private String phone = "";

    @Column(name = "EMAIL", columnDefinition = "VARCHAR(30)")
    private String email;

    @Column(name = "IDDEVICE", columnDefinition = "VARCHAR(50)")
    private String idDevice = "";

    @Column(name = "VERSION", columnDefinition = "VARCHAR(10)")
    private String version = "";

    @Column(name = "CATEGORY", columnDefinition = "VARCHAR(2)")
    private String category = "";

    @Column(name = "SPECIALTY", columnDefinition = "SMALLINT")
    private Short specialty = 0;

    @Column(name = "CONFIRMTYPE", columnDefinition = "SMALLINT")
    private Short confirmType = 0;

    @Column(name = "NATIONALITY", columnDefinition = "SMALLINT")
    private Short nationality;

    @Column(name = "DEPARTMENT", columnDefinition = "SMALLINT")
    private Short department;

    @Column(name = "CITIZENSHIP", columnDefinition = "SMALLINT")
    private Short citizenship;

    @Column(name = "GENDER", columnDefinition = "VARCHAR(30) CCSID 1208")
    private String gender;

    @Column(name = "ORGANIZATION", columnDefinition = "SMALLINT")
    private Short organization = 0;

    @Column(name = "JOBTITLE", columnDefinition = "SMALLINT")
    private Short jobTitle = 0;

    @Column(name = "ISCONFIRMED", columnDefinition = "SMALLINT DEFAULT 0")
    private Short isConfirmed = 0;

    @Column(name = "CONTRACT_NO", columnDefinition = "VARCHAR(20)")
    private String contractNo;

    @Column(name = "CONTRACT_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date contractDate;

    @Column(name = "CONTRACT_END")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date contractEnd;

    @Column(name = "PHOTO", length = 5242880)
    @Lob
    private byte[] photo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
