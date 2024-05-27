package id.co.map.spk.entities;

/**
 * @author Awie on 11/26/2019
 */
public class UserCompanyEntity {

    private Integer userCompanyId;
    private String userName;
    private String companyId;
    private String sbuId;
    private String dataTimeStamp;

    public Integer getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(Integer userCompanyId) {
        this.userCompanyId = userCompanyId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSbuId() {
        return sbuId;
    }

    public void setSbuId(String sbuId) {
        this.sbuId = sbuId;
    }

    public String getDataTimeStamp() {
        return dataTimeStamp;
    }

    public void setDataTimeStamp(String dataTimeStamp) {
        this.dataTimeStamp = dataTimeStamp;
    }
}
