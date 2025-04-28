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

@Entity(name = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",schema = "uat")
public class User extends AbstractAuditingEntity implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid4")
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(50)")
    private String id;

    @Column(nullable = false, name = "username", columnDefinition = "VARCHAR(20)")
    private String username;

    @Column(nullable = false, name = "password", columnDefinition = "VARCHAR(100)")
    private String password;

    @Column(nullable = false, name = "passwordopen", columnDefinition = "VARCHAR(20)")
    private String passwordOpen;

    @Column(name = "pinfl", columnDefinition = "VARCHAR(14)")
    private String pinfl;

    @Column(name = "firstname", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String firstName;

    @Column(name = "lastname", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String lastName;

    @Column(name = "fathername", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String fatherName;

    @Column(name = "fullname", columnDefinition = "VARCHAR(1800) CCSID 1208")
    private String fullName;

    @Column(name = "passport_no", columnDefinition = "VARCHAR(10)")
    private String passportNo = "";

    @Column(name = "passport_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date passportDate;

    @Column(name = "passport_issued", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String passportIssued = "";

    @Column(name = "passport_birthdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date passportBirthDate;

    @Column(name = "address", columnDefinition = "VARCHAR(600) CCSID 1208")
    private String address = "";

    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    private String phone = "";

    @Column(name = "email", columnDefinition = "VARCHAR(30)")
    private String email;

    @Column(name = "iddevice", columnDefinition = "VARCHAR(50)")
    private String idDevice = "";

    @Column(name = "version", columnDefinition = "VARCHAR(10)")
    private String version = "";

    @Column(name = "category", columnDefinition = "VARCHAR(2)")
    private String category = "";

    @Column(name = "specialty", columnDefinition = "SMALLINT")
    private Short specialty = 0;

    @Column(name = "confirmtype", columnDefinition = "SMALLINT")
    private Short confirmType = 0;

    @Column(name = "nationality", columnDefinition = "SMALLINT")
    private Short nationality;

    @Column(name = "department", columnDefinition = "SMALLINT")
    private Short department;

    @Column(name = "citizenship", columnDefinition = "SMALLINT")
    private Short citizenship;

    @Column(name = "gender", columnDefinition = "VARCHAR(30) CCSID 1208")
    private String gender;

    @Column(name = "organization", columnDefinition = "SMALLINT")
    private Short organization = 0;

    @Column(name = "jobtitle", columnDefinition = "SMALLINT")
    private Short jobTitle = 0;

    @Column(name = "isconfirmed", columnDefinition = "SMALLINT DEFAULT 0")
    private Short isConfirmed = 0;

    @Column(name = "contract_no", columnDefinition = "VARCHAR(20)")
    private String contractNo;

    @Column(name = "contract_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date contractDate;

    @Column(name = "contract_end")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date contractEnd;

    @Column(name = "photo", length = 5242880)
    @Lob
    private byte[] photo;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLES",
            schema = "uat",
            joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
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
