package id.co.map.spk.entities;

/**
 * @author Awie on 11/4/2019
 */
public class UserRegistrationEntity {

    private String registrationKey;
    private String userName;
    private String firstName;
    private String lastName;
    private String registrationStatus;
    private String registeredBy;
    private String activatedDateTime;
    private String registrationTimeStamp;

    public String getRegistrationKey() {
        return registrationKey;
    }

    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public String getActivatedDateTime() {
        return activatedDateTime;
    }

    public void setActivatedDateTime(String activatedDateTime) {
        this.activatedDateTime = activatedDateTime;
    }

    public String getRegistrationTimeStamp() {
        return registrationTimeStamp;
    }

    public void setRegistrationTimeStamp(String registrationTimeStamp) {
        this.registrationTimeStamp = registrationTimeStamp;
    }
}
