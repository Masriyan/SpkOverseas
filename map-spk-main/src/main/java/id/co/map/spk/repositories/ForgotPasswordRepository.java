package id.co.map.spk.repositories;

import id.co.map.spk.entities.ForgotPasswordEntity;

import java.sql.SQLException;

/**
 * @author Awie on 3/2/2020
 */
public interface ForgotPasswordRepository {

    ForgotPasswordEntity save(String userName, String email, String status) throws ClassNotFoundException, SQLException;
    ForgotPasswordEntity findByUserNameAndStatus(String userName, String status) throws ClassNotFoundException, SQLException;
    void updateStatusByKey(String key, String status) throws ClassNotFoundException, SQLException;
    ForgotPasswordEntity findByKey(String key) throws  ClassNotFoundException, SQLException;

}
