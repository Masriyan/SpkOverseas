package id.co.map.spk.repositories;

import id.co.map.spk.entities.UserCompanyEntity;

import java.util.List;

/**
 * @author Awie on 11/26/2019
 */
public interface UserCompanyRepository {

    List<UserCompanyEntity> findByUserName(String userName);

}
