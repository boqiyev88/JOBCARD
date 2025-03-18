package uz.uat.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "NAME", columnDefinition = "VARCHAR(300) CCSID 1208")
    private String name;

    @Column(name = "CODE", columnDefinition = "VARCHAR(3)")
    private String code;

    @Column(name = "SYSID", columnDefinition = "VARCHAR(4)")
    private String sysid;

    @Column(name = "SYSTEMS", columnDefinition = "VARCHAR(10)")
    private String systems;

    @Column(name = "SYSTEM_NAME", columnDefinition = "VARCHAR(300) CCSID 1208")
    private String systemName;

    @Column(name = "ROLETYPE", columnDefinition = "SMALLINT")
    private Integer roletype;

    @Override
    public String getAuthority() {
        return name;
    }
}