package id.co.map.spk.jpa;

import javax.persistence.*;

@Entity
@Table(name = "UserRole", uniqueConstraints = {@UniqueConstraint(name = "UserRole_UN", columnNames = {"UserName", "RoleId"})})
public class UserRoleJpa {

    @Id
    @GeneratedValue
    @Column(name = "UserRoleId", nullable = false)
    private Integer userRoleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserName", nullable = false)
    private AppUserJpa appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RoleId", nullable = false)
    private AppRoleJpa appRole;

    @Column(name = "DataTimeStamp")
    private String dataTimeStamp;

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public AppUserJpa getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserJpa appUser) {
        this.appUser = appUser;
    }

    public AppRoleJpa getAppRole() {
        return appRole;
    }

    public void setAppRole(AppRoleJpa appRole) {
        this.appRole = appRole;
    }

    public String getDataTimeStamp() {
        return dataTimeStamp;
    }

    public void setDataTimeStamp(String dataTimeStamp) {
        this.dataTimeStamp = dataTimeStamp;
    }
}
