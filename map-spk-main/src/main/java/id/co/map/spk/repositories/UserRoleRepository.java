package id.co.map.spk.repositories;

import id.co.map.spk.entities.UserRoleEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Awie on 8/27/2019
 */
public interface UserRoleRepository {

    List<UserRoleEntity> findByUser(String userName);
    List<UserRoleEntity> findByRoleId(Integer roleId);
    void updateUserRole(Integer roleId, String username) throws ClassNotFoundException, SQLException;
}
