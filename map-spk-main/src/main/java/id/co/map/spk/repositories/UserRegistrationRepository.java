package id.co.map.spk.repositories;

import id.co.map.spk.entities.UserRegistrationEntity;

/**
 * @author Awie on 11/4/2019
 */
public interface UserRegistrationRepository {

    UserRegistrationEntity findByKey(String registrationKey);
    UserRegistrationEntity findByUserName(String userName);
}
