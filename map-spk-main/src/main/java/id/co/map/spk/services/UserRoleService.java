package id.co.map.spk.services;

import id.co.map.spk.model.response.ClientUserResponse;

/**
 * @author Awie on 11/7/2019
 */
public interface UserRoleService {

    ClientUserResponse updateUserRole(Integer roleId, String userName);

}
