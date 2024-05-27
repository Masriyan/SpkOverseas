package id.co.map.spk.jpa;

import javax.persistence.*;

@Entity
@Table(name = "AppRole", uniqueConstraints = {@UniqueConstraint(name = "AppRole_UN", columnNames = "RoleName")})
public class AppRoleJpa {

    @Id
    @GeneratedValue
    @Column(name = "RoleId", nullable = false)
    private Long roleId;

    @Column(name = "RoleName", length = 50, nullable = false)
    private String roleName;

    @Column(name = "DataTimeStamp")
    private String dataTimeStamp;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDataTimeStamp() {
        return dataTimeStamp;
    }

    public void setDataTimeStamp(String dataTimeStamp) {
        this.dataTimeStamp = dataTimeStamp;
    }
}
