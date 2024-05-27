package id.co.map.spk.entities;

/**
 * @author Awie on 8/27/2019
 */
public class UserRoleEntity {

    private Integer userRoleId;
    private String userName;
    private Integer roleId;
    private String dataTimeStamp;

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getDataTimeStamp() {
        return dataTimeStamp;
    }

    public void setDataTimeStamp(String dataTimeStamp) {
        this.dataTimeStamp = dataTimeStamp;
    }
}
