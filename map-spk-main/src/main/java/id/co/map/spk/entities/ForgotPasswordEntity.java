package id.co.map.spk.entities;

/**
 * @author Awie on 3/2/2020
 */
public class ForgotPasswordEntity {

    private String forgotPasswordKey;
    private String userName;
    private String email;
    private String forgotPasswordStatus;
    private String updatedDateTime;
    private String createdDateTime;

    public String getForgotPasswordKey() {
        return forgotPasswordKey;
    }

    public void setForgotPasswordKey(String forgotPasswordKey) {
        this.forgotPasswordKey = forgotPasswordKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getForgotPasswordStatus() {
        return forgotPasswordStatus;
    }

    public void setForgotPasswordStatus(String forgotPasswordStatus) {
        this.forgotPasswordStatus = forgotPasswordStatus;
    }

    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(String updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
